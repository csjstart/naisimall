package com.chen.naisimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chen.naisimall.product.service.CategoryBrandRelationService;
import com.chen.naisimall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.utils.PageUtils;
import com.chen.common.utils.Query;

import com.chen.naisimall.product.dao.CategoryDao;
import com.chen.naisimall.product.entity.CategoryEntity;
import com.chen.naisimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redisson;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1,查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2,组装成父子的树形结构
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChindrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        //2.1),找出所有的一级分类
        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {

        //TODO  1,检查当前删除的菜单,是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * [1,23,454]
     *
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        CategoryEntity byId = this.getById(catelogId);

        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有的关联的数据
     *  @CacheEvict:失效模式
     *  1,同时进行多种缓存操作@Caching
     *  2,指定删除某个分区下所有的数据,@CacheEvict(value = "category",allEntries = true)
     *  3,存储同一类型的数据,都可以指定成一个分区,分区名默认就是缓存的前缀
     * @param category
     */

    //@Caching(evict = {
    //        @CacheEvict(value = "catelog",key = "'getLevel1Categorys'"),
    //        @CacheEvict(value = "catelog",key = "'getCatelogJson'")
    //})
    //@CachePut     //双写模式
    @CacheEvict(value = "category",allEntries = true)   //失效模式
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

        //1,同时修改缓存中的数据
        //2,redis.del("catelogJSON"),等待下次主动查询进行更新
    }

    //

    /**
     * 1,每一个需要缓存的数据,我们都来指定要放到那个名字的缓存,[缓存的分区(按照业务类型区分)]
     * 2,@Cacheable({"catelog"})
     *      代表当前方法的结果需要缓存,如果缓存中有,方法不用调用,
     *      如果缓存中没有,会调用方法,最后将方法的结果放入缓存
     * 3,默认行为
     *      1),如果缓存中有,方法不再调用
     *      2),key默认自动生成,缓存的名字SimpleKey [](自主生成的key值)
     *      3),缓存的value的值,默认使用jdk序列化机制,将序列化后的数据存到redis
     *      4),默认ttl时间 -1:(永不过期)
     *    自定义:
     *      1),指定生成的缓存使用的key:   key属性指定,接受一个SpEL
     *          SpEl语法看官网   https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html
     *      2),指定缓存的数据的存活时间     配置文件中修改ttlspring.cache.redis.time-to-live=3600000
     *      3),将数据保存为json格式
     *
     *
     *
     * @return
     */
    @Cacheable(value = {"category"},key = "#root.method.name")
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("getLevel1Categorys....");
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        //return null;
        return categoryEntities;
    }

    @Cacheable(value = "category",key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        System.out.println("查询了数据库");
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        //1,查出所有的一级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        //2,封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1,每一个的一级分类,查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            // 2,封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1,找当前二级分类的三级分类封装成Vo
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {

                        List<Catelog2Vo.catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            //2,封装成指定格式
                            Catelog2Vo.catelog3Vo catelog3Vo = new Catelog2Vo.catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatelog3List(collect);


                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));

        return parent_cid;
    }

    /**
     * TODO 产生堆外内存溢出:OurOfectMemoryError
     * 1),springboot2.0以后默认使用lettuce作为操作redis的客户端,它使用netty进行网络通信
     * 2),lettuce的bug导致netty堆外内存溢出 ,-Xmx300m;netty如果没有指定堆外内存,默认使用-Xmx300m
     * 3),可以通过-Dio.netty.maxDirectMemory进行设置
     * 解决方案:不能使用-Dio.netty.maxDirectMemory只能调大堆外内存
     * 1),升级lettuce
     * 2),切换使用jedis
     *
     * @return
     */
    //@Override
    public Map<String, List<Catelog2Vo>> getCatelogJson2() {
        //给缓存中放json字符串,拿出的json字符串,还用逆转为能用的对象类型,[序列化与反序列化]
        /**
         * 1,空结果缓存,解决缓存穿透
         * 2,设置过期时间(加随机值):解决缓存雪崩
         * 3,加锁,解决缓存击穿
         */
        //1,加入缓存逻辑,缓存中存的数据是json字符串
        //JSON跨语言,跨平台兼容
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)) {
            System.out.println("缓存不命中.....查询数据库");
            //缓存中没有,查询数据库
            Map<String, List<Catelog2Vo>> catelogJsonFromDb = getCatelogJsonFromDbWithRedisLock();

            return catelogJsonFromDb;
        }
        System.out.println("缓存命中....直接返回");
        //转为我们制定的对象,
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });

        return result;
    }

    /**
     * 缓存里面的数据如何和数据库的保持一致
     * 缓存数据一致性
     * 1),双写模式
     * 2).失效模式
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedissonLock() {

        //1,锁的名字,锁的粒度,越细越快,
        //锁的粒度,具体缓存的是某个数据 11号商品:product-11-lock
        RLock lock = redisson.getLock("catelogJson-lock");
        lock.lock();

        Map<String, List<Catelog2Vo>> dataFromDb;
        try {
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return dataFromDb;
    }


    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedisLock() {

        //1,占分布式锁,去Redis占坑
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            System.out.println("获取分布式锁成功");
            //加锁成功...执行业务
            //2,设置过期时间(这里出异常有问题)->必须和加锁是同步的,原子操作
            //redisTemplate.expire("lock",30,TimeUnit.SECONDS);
            Map<String, List<Catelog2Vo>> dataFromDb;
            try {
                dataFromDb = getDataFromDb();
            } finally {
                // lua 脚本解锁
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                //删除锁
                Long lock1 = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                        Arrays.asList("lock"), uuid);
            }
            /**
             * 获取值对比+对比成功删除=原子操作
             * String lockValue = redisTemplate.opsForValue().get("lock");
             *             if (uuid.equals(lockValue)){
             *                 //删除锁我自己的锁
             *                 redisTemplate.delete("lock");
             *             }
             */
            return dataFromDb;
        } else {
            //加锁失败...重试,syn
            //休眠100ms重试

            System.out.println("获取分布式锁失败...等待重试");
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getCatelogJsonFromDbWithRedisLock();//自旋的方式

        }
    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            //缓存不为空,直接返回
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }
        System.out.println("查询了数据库");

        /**
         * 1,将数据库的多次查询变为一次
         */
        List<CategoryEntity> selectList = baseMapper.selectList(null);


        //1,查出所有的一级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        //2,封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1,每一个的一级分类,查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            // 2,封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1,找当前二级分类的三级分类封装成Vo
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {

                        List<Catelog2Vo.catelog3Vo> collect = level3Catelog.stream().map(l3 -> {
                            //2,封装成指定格式
                            Catelog2Vo.catelog3Vo catelog3Vo = new Catelog2Vo.catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatelog3List(collect);


                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));

        //3,查到的数据再放入缓存,将对象转为json放在缓存
        String s = JSON.toJSONString(parent_cid);
        redisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);

        return parent_cid;
    }


    /**
     * 从数据库查询并封装数据
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithLocalLock() {
        /**
         * 优化1
         * 只要是同一把锁,就能锁住需要这个锁的所有线程
         * 1,synchronized(this),SpringBoot所有的组件在容器中都是单例的
         * TODO 本地锁,synchronized,JUC(Lock),在分布式情况下,想要锁住所有,必须使用分布式锁
         */
        synchronized (this) {

            /**
             * 得到锁以后,我们应该再去缓存中确定一次如果没有,才需要继续查询
             */
            return getDataFromDb();

        }

    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid().equals(parent_cid)).collect(Collectors.toList());
        return collect;
        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1,手机当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    /**
     * 递归查找所有菜单的子菜单
     */
    private List<CategoryEntity> getChindrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(categoryEntity -> {
            //1,找到子菜单
            categoryEntity.setChildren(getChindrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2,菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

}