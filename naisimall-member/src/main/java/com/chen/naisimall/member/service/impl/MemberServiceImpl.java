package com.chen.naisimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.common.utils.HttpUtils;
import com.chen.naisimall.member.dao.MemberLevelDao;
import com.chen.naisimall.member.entity.MemberLevelEntity;
import com.chen.naisimall.member.exception.PhoneExistException;
import com.chen.naisimall.member.exception.UsernameExistException;
import com.chen.naisimall.member.vo.MemberLoginVo;
import com.chen.naisimall.member.vo.MemberRegistVo;
import com.chen.naisimall.member.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.utils.PageUtils;
import com.chen.common.utils.Query;

import com.chen.naisimall.member.dao.MemberDao;
import com.chen.naisimall.member.entity.MemberEntity;
import com.chen.naisimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) {
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = new MemberEntity();
        //设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());

        //检查用户名和手机号是否唯一,为了让controller能感知异常,异常机制
        checkPhoneUnique(vo.getPhone());
        checkUserNameUnique(vo.getUserName());

        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());

        //密码要进行加密存储
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);

        //其他默认信息,设置
        entity.setNickname(vo.getUserName());

        //保存
        int insert = memberDao.insert(entity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        MemberDao memberDao = this.baseMapper;
        Integer mobile = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (mobile > 0){
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUserNameUnique(String userName) throws PhoneExistException {
        MemberDao memberDao = this.baseMapper;
        Integer username = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (username > 0){
            throw new UsernameExistException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();
        //select * FROM ums_member WHERE mobile = ? or username = ?
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginacct)
                .or().eq("mobile", loginacct));
        if (entity == null){
            //登录失败
            return null;
        }else {
            //1,获取到数据库的password
            String passwordDb = entity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //2,密码匹配
            boolean matches = passwordEncoder.matches(vo.getPassword(), passwordDb);
            if (matches){
                return entity;
            }else {
                return null;
            }

        }

    }

    @Override
    public MemberEntity login(SocialUser socialUser) throws Exception {

        //登录和
        String uid = socialUser.getUid();

        //1,判断当前社交用户是否已经登陆过系统
        MemberDao memberDao = this.baseMapper;
        MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));

        if (memberEntity != null){
            //这个用户已经注册
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(socialUser.getAccess_token());
            update.setExpiresIn(socialUser.getExpires_in());

            memberDao.updateById(update);

            memberEntity.setAccessToken(socialUser.getAccess_token());
            memberEntity.setExpiresIn(socialUser.getExpires_in());

            return memberEntity;
        }else {
            //2,没有查到当前社交用户对应的记录我们就需要注册一个
            MemberEntity regist = new MemberEntity();

            //3,查询当前社交用户的社交帐号信息,(昵称,性别等)
            try {
                Map<String,String> query = new HashMap();
                query.put("access_token",socialUser.getAccess_token());
                query.put("uid",socialUser.getUid());
                HttpResponse response = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<String, String>(), query);
                if (response.getStatusLine().getStatusCode() == 200){
                    //查询成功
                    String json = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(json);
                    //昵称
                    String name = jsonObject.getString("name");
                    String gender = jsonObject.getString("gender");
                    //..............
                    regist.setNickname(name);
                    regist.setGender("m".equals(gender)?1:0);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            regist.setSocialUid(socialUser.getUid());
            regist.setAccessToken(socialUser.getAccess_token());
            regist.setExpiresIn(socialUser.getExpires_in());

            memberDao.insert(regist);
            return regist;
        }
    }

}