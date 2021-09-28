package com.chen.naisimall.product;

//import com.aliyun.oss.OSSClient;

import com.chen.naisimall.product.dao.AttrGroupDao;
import com.chen.naisimall.product.dao.SkuSaleAttrValueDao;
import com.chen.naisimall.product.entity.BrandEntity;
import com.chen.naisimall.product.service.AttrGroupService;
import com.chen.naisimall.product.service.BrandService;
import com.chen.naisimall.product.service.CategoryService;
import com.chen.naisimall.product.vo.SkuItemSaleAttrVo;
import com.chen.naisimall.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
class NaisimallProductApplicationTests {

    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;
    @Test
    public void test01(){
        List<SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(13L);
        System.out.println(saleAttrsBySpuId);
    }


    @Test
    public void test(){
        List<SpuItemAttrGroupVo> attrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(1300L, 225L);
        System.out.println(attrsBySpuId);
    }

    @Test
    public void redisson(){
        System.out.println(redissonClient);
    }

    @Test
    public void stringRedisTemplate() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello","world_"+ UUID.randomUUID().toString());

        String hello = ops.get("hello");
        System.out.println("之前保存的数据:"+hello);
    }

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径:{}", Arrays.asList(catelogPath));
    }

//    @Autowired
//    OSS ossClient;
//
//    @Test
//    public void testUpload() throws FileNotFoundException {
//        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        InputStream inputStream = new FileInputStream("C:\\Users\\woita\\Desktop\\6.jpg");
//        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
//        ossClient.putObject("naisimall", "girl1.jpg", inputStream);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//        System.out.println("上传成功");
//    }


    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("荣耀");
        brandService.updateById(brandEntity);
        System.out.println("保存成功");
    }

}
