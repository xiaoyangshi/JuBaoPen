
package com.jinjiuyun.miban.greendao;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jinjiuyun.miban.db.Preferences;
import com.jinjiuyun.miban.im.model.BaseMyRelationMessageModel;
import com.jinjiuyun.miban.im.model.BaseSystemMessageModel;
import com.jinjiuyun.miban.im.model.MyCommentMessageModel;
import com.jinjiuyun.miban.im.model.MyFollowMessageModel;
import com.jinjiuyun.miban.im.model.MyGradeMessageModel;
import com.jinjiuyun.miban.im.model.MyLikeMessageModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lty on 2018/2/5.
 */

public class OperateDB {
    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param robOrder
     */
    public static void insertRobOrder(RobOrder robOrder) {
        GreenDaoSetUp.getDaoInstant().getRobOrderDao().insertOrReplace(robOrder);
    }

    /**
     * 删除所有数据
     */
    public static void deleteAllRobOrder() {
        GreenDaoSetUp.getDaoInstant().getRobOrderDao().deleteAll();
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void delete(long id) {
        GreenDaoSetUp.getDaoInstant().getRobOrderDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param robOrder
     */
    public static void update(RobOrder robOrder) {
        GreenDaoSetUp.getDaoInstant().getRobOrderDao().update(robOrder);
    }

    /*  *//**
           * 查询条件为Type=TYPE_LOVE的数据
           *
           * @return
           *//*
              * public static List<RobOrder> queryLove() { return
              * GreenDaoSetUp.getDaoInstant().getRobOrderDao().queryBuilder().where(RobOrder.
              * Properties.Type.eq(RobOrder.TYPE_LOVE)).list(); }
              */

    /**
     * 查询全部数据
     */
    public static List<RobOrder> queryAll() {
        return GreenDaoSetUp.getDaoInstant().getRobOrderDao().loadAll();
    }

    public static List<BaseSystemMessageModel> queryUnReadSystemMessage() {
        List<SystemMessageEntity> list = GreenDaoSetUp.getDaoInstant().getSystemMessageEntityDao()
                .queryBuilder().where(SystemMessageEntityDao.Properties.IsUnread.eq(true)).list();
        LinkedList<BaseSystemMessageModel> systemMessageModels = new LinkedList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                BaseSystemMessageModel baseSystemMessageModel = new Gson()
                        .fromJson(list.get(i).getData(), BaseSystemMessageModel.class);
                baseSystemMessageModel.setTime(list.get(i).getTime());
                systemMessageModels.addFirst(baseSystemMessageModel);
            }
        }
        return systemMessageModels;
    }

    public static List<BaseSystemMessageModel> queryAllSystemMessage() {
        List<SystemMessageEntity> list = GreenDaoSetUp.getDaoInstant().getSystemMessageEntityDao()
                .loadAll();
        LinkedList<BaseSystemMessageModel> systemMessageModels = new LinkedList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                BaseSystemMessageModel baseSystemMessageModel = new Gson()
                        .fromJson(list.get(i).getData(), BaseSystemMessageModel.class);
                baseSystemMessageModel.setTime(list.get(i).getTime());
                systemMessageModels.addFirst(baseSystemMessageModel);
            }
        }
        return systemMessageModels;
    }

    public static BaseSystemMessageModel getLastSystemMessage() {
        List<BaseSystemMessageModel> list = queryAllSystemMessage();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public static void clearAllSystemMessage() {
        GreenDaoSetUp.getDaoInstant().getSystemMessageEntityDao().deleteAll();
    }

    public static void insertSystemMessage(String data) {
        // GreenDaoSetUp.getDaoInstant().getSystemMessageEntityDao().insert(systemMessageEntity);
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SystemMessageEntity systemMessageEntity = new SystemMessageEntity();
        systemMessageEntity.setTime(System.currentTimeMillis());
        systemMessageEntity.setData(data);
        systemMessageEntity.setIsUnread(true);
        int unreadCount = Preferences.getInstance().getInt(Preferences.KEY_SYSTEM_MESSAGE_UNREAD,
                0);
        Preferences.getInstance().putInt(Preferences.KEY_SYSTEM_MESSAGE_UNREAD, unreadCount + 1);
        insertSystemMessage(systemMessageEntity);
    }

    public static void setSystemMessageRead() {
        List<SystemMessageEntity> list = GreenDaoSetUp.getDaoInstant().getSystemMessageEntityDao()
                .queryBuilder().where(SystemMessageEntityDao.Properties.IsUnread.eq(true)).list();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIsUnread(false);
            GreenDaoSetUp.getDaoInstant().getSystemMessageEntityDao().update(list.get(i));
        }
    }

    public static void insertSystemMessage(SystemMessageEntity systemMessageEntity) {
        GreenDaoSetUp.getDaoInstant().getSystemMessageEntityDao().insert(systemMessageEntity);
    }

    public static List<BaseMyRelationMessageModel> queryAllMyRelationMessage() {
        List<MyRelationMessageEntity> list = GreenDaoSetUp.getDaoInstant()
                .getMyRelationMessageEntityDao().loadAll();
        LinkedList<BaseMyRelationMessageModel> msgList = new LinkedList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType() == BaseMyRelationMessageModel.MESSAGE_TYPE_COMMENT) {
                    MyCommentMessageModel myCommentMessageModel = new Gson()
                            .fromJson(list.get(i).getData(), MyCommentMessageModel.class);
                    myCommentMessageModel.setTime(list.get(i).getTime());
                    msgList.addFirst(myCommentMessageModel);
                } else if (list.get(i)
                        .getType() == BaseMyRelationMessageModel.MESSAGE_TYPE_FOLLOW) {
                    MyFollowMessageModel myGradeMessageModel = new Gson()
                            .fromJson(list.get(i).getData(), MyFollowMessageModel.class);
                    myGradeMessageModel.setTime(list.get(i).getTime());
                    msgList.addFirst(myGradeMessageModel);
                } else if (list.get(i).getType() == BaseMyRelationMessageModel.MESSAGE_TYPE_GRADE) {
                    MyGradeMessageModel myGradeMessageModel = new Gson()
                            .fromJson(list.get(i).getData(), MyGradeMessageModel.class);
                    myGradeMessageModel.setTime(list.get(i).getTime());
                    msgList.addFirst(
                            new Gson().fromJson(list.get(i).getData(), MyGradeMessageModel.class));
                } else if (list.get(i).getType() == BaseMyRelationMessageModel.MESSAGE_TYPE_LIKE) {
                    MyLikeMessageModel myLikeMessageModel = new Gson()
                            .fromJson(list.get(i).getData(), MyLikeMessageModel.class);
                    myLikeMessageModel.setTime(list.get(i).getTime());
                    msgList.addFirst(
                            new Gson().fromJson(list.get(i).getData(), MyLikeMessageModel.class));
                }
            }
        }
        return msgList;
    }

    public static void clearAllMyRelationMessage() {
        GreenDaoSetUp.getDaoInstant().getMyRelationMessageEntityDao().deleteAll();
    }

    public static void insertMyRelationMessage(MyRelationMessageEntity myRelationMessageEntity) {
        GreenDaoSetUp.getDaoInstant().getMyRelationMessageEntityDao()
                .insert(myRelationMessageEntity);
    }

    public static void insertMyRelationMessage(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(data);
            int subType = jsonObject.getInt(BaseMyRelationMessageModel.KEY_SUBTYPE);
            MyRelationMessageEntity myRelationMessageEntity = new MyRelationMessageEntity();
            myRelationMessageEntity.setType(subType);
            myRelationMessageEntity.setTime(System.currentTimeMillis());
            myRelationMessageEntity.setData(data);
            myRelationMessageEntity.setIsUnread(true);
            int unreadCount = Preferences.getInstance().getInt(Preferences.KEY_MY_MESSAGE_UNREAD,
                    0);
            Preferences.getInstance().putInt(Preferences.KEY_MY_MESSAGE_UNREAD, unreadCount + 1);
            insertMyRelationMessage(myRelationMessageEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<BaseMyRelationMessageModel> queryUnReadMyMessage() {
        List<MyRelationMessageEntity> list = GreenDaoSetUp.getDaoInstant()
                .getMyRelationMessageEntityDao().queryBuilder()
                .where(SystemMessageEntityDao.Properties.IsUnread.eq(true)).list();
        LinkedList<BaseMyRelationMessageModel> myMessageModels = new LinkedList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType() == BaseMyRelationMessageModel.MESSAGE_TYPE_COMMENT) {
                    MyCommentMessageModel myCommentMessageModel = new Gson()
                            .fromJson(list.get(i).getData(), MyCommentMessageModel.class);
                    myCommentMessageModel.setTime(list.get(i).getTime());
                    myMessageModels.addFirst(myCommentMessageModel);
                } else if (list.get(i)
                        .getType() == BaseMyRelationMessageModel.MESSAGE_TYPE_FOLLOW) {
                    MyFollowMessageModel myGradeMessageModel = new Gson()
                            .fromJson(list.get(i).getData(), MyFollowMessageModel.class);
                    myGradeMessageModel.setTime(list.get(i).getTime());
                    myMessageModels.addFirst(myGradeMessageModel);
                } else if (list.get(i).getType() == BaseMyRelationMessageModel.MESSAGE_TYPE_GRADE) {
                    MyGradeMessageModel myGradeMessageModel = new Gson()
                            .fromJson(list.get(i).getData(), MyGradeMessageModel.class);
                    myGradeMessageModel.setTime(list.get(i).getTime());
                    myMessageModels.addFirst(
                            new Gson().fromJson(list.get(i).getData(), MyGradeMessageModel.class));
                } else if (list.get(i).getType() == BaseMyRelationMessageModel.MESSAGE_TYPE_LIKE) {
                    MyLikeMessageModel myLikeMessageModel = new Gson()
                            .fromJson(list.get(i).getData(), MyLikeMessageModel.class);
                    myLikeMessageModel.setTime(list.get(i).getTime());
                    myMessageModels.addFirst(
                            new Gson().fromJson(list.get(i).getData(), MyLikeMessageModel.class));
                }
            }
        }
        return myMessageModels;
    }

    public static void setMyMessageRead() {
        List<MyRelationMessageEntity> list = GreenDaoSetUp.getDaoInstant()
                .getMyRelationMessageEntityDao().loadAll();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIsUnread(false);
            GreenDaoSetUp.getDaoInstant().getMyRelationMessageEntityDao().update(list.get(i));
        }
    }

}
