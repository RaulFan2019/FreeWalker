package cn.yy.freewalker.utils;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/10 21:51
 */
public class UserInfoU {


    /**
     * 获取体重字符串
     *
     * @param weightIndex
     * @return
     */
    public static String getWeightStr(final int weightIndex) {
        return getWeightStrList().get(weightIndex);
    }


    /**
     * 体重字符串列表
     *
     * @return
     */
    public static ArrayList<String> getWeightStrList() {
        ArrayList<String> listWeightSelect = new ArrayList<>();
        listWeightSelect.add("40+");
        listWeightSelect.add("50+");
        listWeightSelect.add("60+");
        listWeightSelect.add("70+");
        listWeightSelect.add("80+");
        listWeightSelect.add("90+");

        return listWeightSelect;
    }


    /**
     * 获取身高字符串
     *
     * @param heightIndex
     * @return
     */
    public static String getHeightStr(final int heightIndex) {
        return getHeightStrList().get(heightIndex);
    }


    /**
     * 获取身高字符串列表
     *
     * @return
     */
    public static ArrayList<String> getHeightStrList() {
        ArrayList<String> listHeightSelect = new ArrayList<>();

        listHeightSelect.add("150+");
        listHeightSelect.add("160+");
        listHeightSelect.add("170+");
        listHeightSelect.add("180+");

        return listHeightSelect;
    }


    /**
     * 获取年龄字符串
     *
     * @param ageIndex
     * @return
     */
    public static String getAgeStr(final int ageIndex) {
        return getAgeStrList().get(ageIndex);
    }


    /**
     * 获取年龄字符串列表
     *
     * @return
     */
    public static ArrayList<String> getAgeStrList() {
        ArrayList<String> listAgeSelect = new ArrayList<>();

        for (int i = 18; i < 30; i++) {
            listAgeSelect.add(i + "");
        }
        listAgeSelect.add("-");

        return listAgeSelect;
    }

    /**
     * 获取年龄字符串
     *
     * @param genderIndex
     * @return
     */
    public static String getGenderStr(final Context context, final int genderIndex) {
        return getGenderStrList(context).get(genderIndex);
    }

    public static List<String> getGenderStrList(final Context context) {
        ArrayList<String> listGenderSelect = new ArrayList<>();
        listGenderSelect.add(context.getString(R.string.auth_tx_sex_male));
        listGenderSelect.add(context.getString(R.string.auth_tx_sex_female));
        listGenderSelect.add(context.getString(R.string.auth_tx_sex_other));
        return listGenderSelect;
    }

    /**
     * 获取年龄字符串
     *
     * @param genderOriIndex
     * @return
     */
    public static String getGenderOriStr(final Context context, final int genderOriIndex) {
        return getGenderOriStrList(context).get(genderOriIndex);
    }

    public static ArrayList<String> getGenderOriStrList(final Context context){
        ArrayList<String> listGenderOriLikeSelect = new ArrayList<>();

        listGenderOriLikeSelect.add(context.getString(R.string.auth_tx_like_male));
        listGenderOriLikeSelect.add(context.getString(R.string.auth_tx_like_female));
        listGenderOriLikeSelect.add(context.getString(R.string.auth_tx_like_all));
        listGenderOriLikeSelect.add(context.getString(R.string.auth_tx_like_other));

        return listGenderOriLikeSelect;
    }


    /**
     * 获取工作字符串
     *
     * @param context
     * @param jobIndex
     * @return
     */
    public static String getJobStr(final Context context, final int jobIndex) {
        return getJobStrList(context).get(jobIndex);
    }

    /**
     * 获取工作字符串列表
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getJobStrList(final Context context) {
        ArrayList<String> listProfessionSelect = new ArrayList<>();

        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_1));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_2));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_3));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_4));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_5));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_6));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_7));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_8));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_9));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_10));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_11));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_12));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_13));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_14));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_15));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_16));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_17));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_18));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_19));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_20));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_21));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_22));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_23));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_24));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_25));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_26));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_27));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_28));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_29));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_30));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_31));
        listProfessionSelect.add(context.getString(R.string.auth_tx_profession_32));

        return listProfessionSelect;
    }
}
