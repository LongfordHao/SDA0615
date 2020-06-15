package com.tfjy.sda.service.impl;

import cn.hutool.core.util.IdUtil;
import com.tfjy.sda.bean.*;
import com.tfjy.sda.mapper.*;
import com.tfjy.sda.service.IntegralService;
import com.tfjy.sda.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Author: LangFordHao
 * Version:V1.0
 * Date: 2020/6/10
 * Time: 10:04
 * Description: 根据加分规则给学生加分
 */
@Service
public class IntegralServiceImpl implements IntegralService {

    @Autowired
    private TopicQuestionMapper topicQuestionMapper;
    @Autowired
    private TopicDetailListMapper topicDetailListMapper;
    @Autowired
    private TopicListMapper topicListMapper;
    @Autowired
    private IntegralMapper integralMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 根据学生发布的讨论主题中的关键子给学生加分，加分对象为问题的发起人。基础分为一分，只要学生发布了讨论主题就有一分。
     */
    private String topicQuestionAdd(String questionContent){


        //设定好的关键字
        String questionItem= PropertiesUtil.getValue("topicQuestion");//{"hahah","shahhs"};//配置文件中的关键字
        String[] questionItems=questionItem.split(",");
        //每个关键字的分数
        String questionFraction= PropertiesUtil.getValue("questionFraction");
        //基础分数
        String fraction= PropertiesUtil.getValue("fraction");
        //***检查主题中有没有关键字，含有几个关键字
        int questionIntegral=containsWords(questionContent,questionItems);
        //只要有回复就有一个基础分
        String sumIntegral= String.valueOf(questionIntegral*Integer.parseInt(questionFraction)+Integer.parseInt(fraction));
        return sumIntegral;
    }

    /**
     * 讨论详情中的加分。加分对象为本条讨论的回复人，发起人给回复人再次回复信息并且包含“解决问题”，给回复人加分
     * 例： 张三老师发起的讨论话题
     *          李四同学提出问题，是个提问者
     *               王五 回复 李四 ： 你的问题解决方案是。。。。
     *               李四 回复 王五 ： “某某某道友帮助吾解决了xxx问题，非常感谢”  （检测关键字为：解决，非常感谢）
     *               赵六 回复 李四 ： “某某某道友帮助吾解决了xxx问题，非常感谢”   （李四不加分，必须是提问者回复关键字才给解决问题的人加分）
     *         以上情况给王五同学加分
     */
    private String topicRplyAdd(String replyContent){
        //设定好的关键字
        String replyItem= PropertiesUtil.getValue("topicReply");//{"解决","感谢"};//配置文件中的关键字


        String replyFraction= PropertiesUtil.getValue("replyFraction");
        String[] replyItems=replyItem.split(",");
        //***检查主题中有没有关键字，含有几个关键字
        int replyIntegral=containsWords(replyContent,replyItems)+Integer.parseInt(replyFraction);
        return String.valueOf(replyIntegral);
    }

    private String topicRplyTwoAdd(String replyContent){
        //设定好的关键字
        String replyItem= PropertiesUtil.getValue("topicReplyTwo");//配置文件中的关键字


        String replyFraction= PropertiesUtil.getValue("replyTwoFraction");
        String[] replyItems=replyItem.split(",");
        //***检查主题中有没有关键字，含有几个关键字
        int replyIntegral=containsWords(replyContent,replyItems)+Integer.parseInt(replyFraction);
        return String.valueOf(replyIntegral);
    }

    /**
     * 主题下的回复中有视频给加分
     * @param voideoContent
     * @return
     */
    private  String videoAdd(String voideoContent){
        //设定好的视频格式
        String videoTem= PropertiesUtil.getValue("topicVideo");
        String[] videoTems=videoTem.split(",");
        //每个视频加的分数
        String videoFraction= PropertiesUtil.getValue("videoFraction");
        int replyIntegral=containsWords(voideoContent,videoTems)*Integer.parseInt(videoFraction);
        return  String.valueOf(replyIntegral);
    }

    private  String pictureAdd(String pictureContent){
        //设定好的图片格式
        String pictureTem= PropertiesUtil.getValue("topicPicture");
        String[] pictureTems=pictureTem.split(",");
        //每个图片加分
        String pictureFraction= PropertiesUtil.getValue("pictureFraction");
        int replyIntegral=containsWords(pictureContent,pictureTems)*Integer.parseInt(pictureFraction);
        return  String.valueOf(replyIntegral);
    }

    /**
     * 加分量
     * @param inputString  标题，或回复内容
     * @param items 加分关键字
     * @return
     */
    private static int containsWords(String inputString, String[] items) {
        int soc=0;
        for (String item : items) {
            if (!inputString.contains(item)) {
                soc=+0;
                break;
            }
            soc= +1;
        }
        return soc;
    }
    @Override
    public void getIntegralInto(){
        //查询学生讨论的主题遍历
        List<TopicQuestion> topicuqestion=topicQuestionMapper.selectAll();
        for (TopicQuestion topicQustion:topicuqestion) {
            //取出讨论主题的内容
            String questionContent= topicQustion.getQuestionContent();
            //找到问题发起者的所有回复
            Example exampleOut=new Example(TopicReply.class);
            exampleOut.createCriteria().andEqualTo("questionId",topicQustion.getId());
            exampleOut.createCriteria().andEqualTo("replyName",topicQustion.getQuestioner());
            List<TopicReply> topicReplies = topicDetailListMapper.selectByExample(exampleOut);
            String topContent="";
            //被加分人姓名
            String integralName=topicQustion.getQuestioner();
            String topicTime=topicQustion.getQuestionTime();
            for (TopicReply topicreply:topicReplies) {
                topContent = topicreply.getReplyContent();
                integralName=topicreply.getReplied();
                topicTime=topicreply.getReplyTim();
                //**************************给单人加分******************************
                //给问题标题下的讨论回复解决问题的人加分
                String topicReplyIntegral=topicRplyAdd(topContent);
                String topicReplyRemarks= PropertiesUtil.getValue("topicReplyRemarks");
                //是否启用此加分规则
                String topicReplyOn= PropertiesUtil.getValue("topicReplyOn");
                if (topicReplyIntegral.equals("0")==false && topicReplyOn.equals("0")==false){
                    insterInto(topicQustion,topicReplyIntegral,topicReplyRemarks,integralName,topicTime);
                }else {
                    System.out.println("不加分1");
                }
                //********************************给两人加分**********************************

                //给问题标题下的讨论回复解决问题的人加分
                String topicReplyTwoIntegral=topicRplyAdd(topContent);
                String topicReplyTwoRemarks= PropertiesUtil.getValue("topicReplyTwoRemarks");
                //是否启用此加分规则
                String topicReplyTwoOn= PropertiesUtil.getValue("topicReplyTwoOn");
                if (topicReplyIntegral.equals("0")==false && topicReplyTwoOn.equals("0")==false){

                    insterInto(topicQustion,topicReplyTwoIntegral,topicReplyTwoRemarks,integralName,topicTime);
                    integralName=topicreply.getReplyName();
                    insterInto(topicQustion,topicReplyTwoIntegral,topicReplyTwoRemarks,integralName,topicTime);
                }else {
                    System.out.println("不加分2");
                }
            }
            //将问题标题中的关键字进行加分
            String questionIntegral= topicQuestionAdd(questionContent);
            String questionRemarks= PropertiesUtil.getValue("topicQuestionRemarks");
            //是否启用此加分规则
            String topicQuestionOn= PropertiesUtil.getValue("topicQuestionOn");
            if (questionIntegral.equals("0")==false && topicQuestionOn.equals("0")==false ){
                insterInto(topicQustion,questionIntegral,questionRemarks,integralName,topicTime);
            }else {
                System.out.println("不加分3");
            }

            //给话题下的回复添加视频加分
            String topicVideoIntegral=videoAdd(topContent);
            String topicVideoRemarks= PropertiesUtil.getValue("topicVideoRemarks");
            //是否启用此加分规则
            String topicVideoOn= PropertiesUtil.getValue("topicVideoOn");
            if (topicVideoIntegral.equals("0")==false && topicVideoOn.equals("0")==false) {
                insterInto(topicQustion, topicVideoIntegral, topicVideoRemarks, integralName,topicTime);
            }else {
                System.out.println("不加分4");
            }
            //给话题下的回复添加图片加分
            String topicPictureIntegral=pictureAdd(topContent);
            String topicPictureRemarks= PropertiesUtil.getValue("topicPictureRemarks");
            //是否启用此加分规则
            String topicPictureOn= PropertiesUtil.getValue("topicPictureOn");
            if (topicPictureIntegral.equals("0")==false && topicPictureOn.equals("0")==false){
                insterInto(topicQustion,topicPictureIntegral,topicPictureRemarks,integralName,topicTime);
            }else {
                System.out.println("不加分5");
            }
        }
    }

    /**
     * 将加分项目存储到数据库中
     * @param topicQustion 某个讨论问题，老师话题下的回复
     * @param integral 加分的分值
     * @param remarks 备注
     * @param integralName 加分人的姓名
     */
    private void  insterInto(TopicQuestion topicQustion,String integral,String remarks,String integralName,String topicTime){
        //查询课程id，并通过课程id来获取学生的学号
        //***通过questionid获取topic_list的外键就是course的id
        Example example=new Example(TopicList.class);
        example.createCriteria().andEqualTo("Id",topicQustion.getTopicId());
        TopicList topicId=topicListMapper.selectOneByExample(example);
        //通过课程id和用户姓名来确定用户的学号（前提是同一个班里没有同名的学生）
        Example userInfo=new Example(UserInfo.class);
        userInfo.createCriteria().andEqualTo("courseId",topicId.getCourseId()).andEqualTo("name",topicQustion.getQuestioner());
        List<UserInfo> userinfoSecl=userInfoMapper.selectByExample(userInfo);

        //最高加15分
        if (Integer.parseInt(integral)>15){
            integral="15";
        }
        if (userinfoSecl.size()!=0){
            //判断将要插入的数据库中没有
            Example exampleIn=new Example(Integral.class);
            exampleIn.createCriteria().andEqualTo("topicId",topicQustion.getId()).andEqualTo("stuNumber",userinfoSecl.get(0).getStudentNumber()).andEqualTo("topicTime",topicTime);
            Integral exercise=integralMapper.selectOneByExample(exampleIn);

            if (exercise!=null){
                exercise.setIntegral(integral);
                exercise.setRemarks(remarks);
                integralMapper.updateByPrimaryKey(exercise);
                System.out.println("该讨论加分已更新");
            }else {
                Integral integralIn=new Integral();
                integralIn.setId(IdUtil.randomUUID());
                integralIn.setIntegral(integral);
                integralIn.setName(integralName);
                integralIn.setStuNumber(userinfoSecl.get(0).getStudentNumber());
                integralIn.setTopicId(topicQustion.getId());//这个id与话题底下的讨论问题相对应
                integralIn.setRemarks(remarks);//备注
                integralIn.setTopicTime(topicTime);
                //将数据插入到加分表中
                integralMapper.insert(integralIn);
                System.out.println("已加分");
            }
        }else {
            System.out.println("数据库中没有这个学生");
        }

    }
}
