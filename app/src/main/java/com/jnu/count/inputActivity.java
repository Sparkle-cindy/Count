package com.jnu.count;

import static com.jnu.count.data.DateUtils.FORMAT_D;
import static com.jnu.count.data.DateUtils.FORMAT_M;
import static com.jnu.count.data.DateUtils.FORMAT_Y;
import static com.jnu.count.data.DateUtils.FORMAT_YMD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jnu.count.data.DateUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class inputActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView money;     //金额
    private TextView method;
    private TextView date;      //时间选择
    private TextView cash;      //收支选择
    private TextView WeChat;
    private ImageView remark;   //

    private ViewPager viewpagerItem;
    private LinearLayout layoutIcon;

    //计算器
    protected boolean isDot;
    protected String num = "0";               //整数部分
    protected String dotNum = ".00";          //小数部分
    protected final int MAX_NUM = 9999999;    //最大整数
    protected final int DOT_NUM = 2;          //小数部分最大位数
    protected int count = 0;



    public boolean isOutcome = true;
    public boolean isEdit = false;
    //old Bill
    private Bundle bundle;

    //选择时间
    protected int Year;
    protected int Month;
    protected int Day;
    protected String days;

    //备注
    protected String remarkInput = "";

    //选择文字
    private TextView other;
    private TextView drink;
    private TextView car;
    private TextView communicate;
    private TextView borrow;
    private TextView gift;
    private TextView home;
    private TextView hospital;
    private TextView society;
    private TextView shopping;
    private TextView sport;
    private TextView salary;
    private TextView diary;
    private TextView food;
    private TextView fund;
    private TextView travel;

    //选择图标
    private ImageView otherIV;
    private ImageView drinkIV;
    private ImageView carIV;
    private ImageView communicateIV;
    private ImageView borrowIV;
    private ImageView giftIV;
    private ImageView homeIV;
    private ImageView hospitalIV;
    private ImageView societyIV;
    private ImageView shoppingIV;
    private ImageView sportIV;
    private ImageView salaryIV;
    private ImageView diaryIV;
    private ImageView foodIV;
    private ImageView fundIV;
    private ImageView travelIV;
    private int res;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        initData();
        initWidget();
        initClick();

        Intent intent=getIntent();
        String i= "0.00";
        String textName=intent.getStringExtra("name");
        String textPrice=intent.getStringExtra("price");
        String textWechat=intent.getStringExtra("wechat");
        String textInOutCome=intent.getStringExtra("inoutCome");
        int pictureId=intent.getIntExtra("PictureID",0);

        if(i!=money.getText().toString()){
            method.setText(textName);
            money.setText(textPrice);
            res=pictureId;
            WeChat.setText(textWechat);
            cash.setText(textInOutCome);
        }
        //设置金额
        money.setText(num + dotNum);
        method.setText("房租水电");
        WeChat.setText("微信");
        cash.setText("支出");

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=cash.getText().toString();
                if("支出"==str)
                    cash.setText("收入");
                else
                    cash.setText("支出");
            }
        });

        WeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(inputActivity.this, v);//第二个参数是绑定的那个view
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.wechat_sort, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.toolbar_WeChat:
                                WeChat.setText("微信");
                                break;
                            case R.id.toolbar_zhiFUBao:
                                WeChat.setText("支付宝");
                                break;
                            case R.id.toolbar_xinJin:
                                WeChat.setText("现金");
                                break;
                            case R.id.toolbar_none:
                                WeChat.setText("无账户");
                                break;
                            case R.id.toolbar_addSort:
                                WeChat.setText("添加账户");
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    protected void initData() {
        //设置日期选择器初始日期
        Year = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        Month = Integer.parseInt(DateUtils.getCurMonth(FORMAT_M));
        Day = Integer.parseInt(DateUtils.getCurDay(FORMAT_D));
        //设置当前日期
        days = DateUtils.getCurDateStr("yyyy-MM-dd");

        bundle = getIntent().getBundleExtra("bundle");

        if (bundle != null) {    //edit
            isEdit = true;
            //设置账单日期
            days = DateUtils.long2Str(bundle.getLong("date"), FORMAT_YMD);
            remarkInput = bundle.getString("content");
            isOutcome = !bundle.getBoolean("income");
            DecimalFormat df = new DecimalFormat("######0.00");
            String money = df.format(bundle.getDouble("cost"));
            //小数取整
            num = money.split("\\.")[0];
            //截取小数部分
            dotNum = "." + money.split("\\.")[1];
        }
    }

    protected void initWidget() {
        money = findViewById(R.id.note_money);
        method=findViewById(R.id.item_type_tv);
        date = findViewById(R.id.note_date);
        cash = findViewById(R.id.note_cash);
        remark = findViewById(R.id.note_remark);
        WeChat=findViewById(R.id.note_WeChat);

        other=findViewById(R.id.item_type_tv_2);
        drink=findViewById(R.id.item_type_tv_3);
        car=findViewById(R.id.item_type_tv_4);
        communicate=findViewById(R.id.item_type_tv_5);
        borrow=findViewById(R.id.item_type_tv_6);
        gift=findViewById(R.id.item_type_tv_7);
        home=findViewById(R.id.item_type_tv_8);
        hospital=findViewById(R.id.item_type_tv_9);
        society=findViewById(R.id.item_type_tv_10);
        shopping=findViewById(R.id.item_type_tv_11);
        sport=findViewById(R.id.item_type_tv_12);
        salary=findViewById(R.id.item_type_tv_13);
        diary=findViewById(R.id.item_type_tv_14);
        food=findViewById(R.id.item_type_tv_15);
        fund=findViewById(R.id.item_type_tv_16);
        travel=findViewById(R.id.item_type_tv_17);


        otherIV=findViewById(R.id.item_type_img_2);;
        drinkIV=findViewById(R.id.item_type_img_3);
        carIV=findViewById(R.id.item_type_img_4);
        communicateIV=findViewById(R.id.item_type_img_5);
        borrowIV=findViewById(R.id.item_type_img_6);
        giftIV=findViewById(R.id.item_type_img_7);
        homeIV=findViewById(R.id.item_type_img_8);
        hospitalIV=findViewById(R.id.item_type_img_9);
        societyIV=findViewById(R.id.item_type_img_10);
        shoppingIV=findViewById(R.id.item_type_img_11);
        sportIV=findViewById(R.id.item_type_img_12);
        salaryIV=findViewById(R.id.item_type_img_13);
        diaryIV=findViewById(R.id.item_type_img_14);
        foodIV=findViewById(R.id.item_type_img_15);
        fundIV=findViewById(R.id.item_type_img_16);
        travelIV=findViewById(R.id.item_type_img_17);

        //设置账单日期
        date.setText(days);
    }

    protected void initClick() {
        date.setOnClickListener(this);
        remark.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.note_cash://现金
                //showPayinfoSelector();
                break;
            case R.id.note_date://日期
                showTimeSelector();
                break;
            case R.id.note_remark://备注
                //showContentDialog();
                break;
            case R.id.tb_calc_num_done://确定
                doCommit();
                break;
            case R.id.tb_calc_num_1:
                calcMoney(1);
                break;
            case R.id.tb_calc_num_2:
                calcMoney(2);
                break;
            case R.id.tb_calc_num_3:
                calcMoney(3);
                break;
            case R.id.tb_calc_num_4:
                calcMoney(4);
                break;
            case R.id.tb_calc_num_5:
                calcMoney(5);
                break;
            case R.id.tb_calc_num_6:
                calcMoney(6);
                break;
            case R.id.tb_calc_num_7:
                calcMoney(7);
                break;
            case R.id.tb_calc_num_8:
                calcMoney(8);
                break;
            case R.id.tb_calc_num_9:
                calcMoney(9);
                break;
            case R.id.tb_calc_num_0:
                calcMoney(0);
                break;
            case R.id.tb_calc_num_dot:
                if (dotNum.equals(".00")) {
                    isDot = true;
                    dotNum = ".";
                }
                money.setText(num + dotNum);
                break;
            case R.id.note_clear://清空
                doClear();
                break;
            case R.id.calc_num_del://删除
                doDelete();
                break; 
            case R.id.item_type_img_2:
                method.setText(other.getText().toString());
                res = R.drawable.icon_02;
                break;
            case R.id.item_type_img_3:
                method.setText(drink.getText().toString());
                res = R.drawable.icon_03;
                break;
            case R.id.item_type_img_4:
                method.setText(car.getText().toString());
                res = R.drawable.icon_04;
                break;
            case R.id.item_type_img_5:
                method.setText(communicate.getText().toString());
                res = R.drawable.icon_05;
                break;
            case R.id.item_type_img_6:
                method.setText(borrow.getText().toString());
                res = R.drawable.icon_06;
                break;
            case R.id.item_type_img_7:
                method.setText(gift.getText().toString());
                res = R.drawable.icon_07;
                break;
            case R.id.item_type_img_8:
                method.setText(home.getText().toString());
                res = R.drawable.icon_08;
                break;
            case R.id.item_type_img_9:
                method.setText(hospital.getText().toString());
                res = R.drawable.icon_09;
                break;
            case R.id.item_type_img_10:
                method.setText(society.getText().toString());
                res = R.drawable.icon_10;
                break;
            case R.id.item_type_img_11:
                method.setText(shopping.getText().toString());
                res = R.drawable.icon_11;
                break;
            case R.id.item_type_img_12:
                method.setText(sport.getText().toString());
                res = R.drawable.icon_12;
                break;
            case R.id.item_type_img_13:
                method.setText(salary.getText().toString());
                res = R.drawable.icon_13;
                break;
            case R.id.item_type_img_14:
                method.setText(diary.getText().toString());
                res = R.drawable.icon_14;
                break;
            case R.id.item_type_img_15:
                method.setText(food.getText().toString());
                res = R.drawable.icon_15;
                break;
            case R.id.item_type_img_16:
                method.setText(fund.getText().toString());
                res = R.drawable.icon_16;
                break;
            case R.id.item_type_img_17:
                method.setText(travel.getText().toString());
                res = R.drawable.icon_17;
                break;
        }
    }

    private void showTimeSelector() {
        new DatePickerDialog(this, (DatePicker datePicker, int i, int i1, int i2) -> {  //显示日期选择器
            Year = i;
            Month = i1;
            Day = i2;
            if (Month + 1 < 10) {
                if (Day < 10) {
                    days = new StringBuffer().append(Year).append("-").append("0").
                            append(Month + 1).append("-").append("0").append(Day).toString();
                } else {
                    days = new StringBuffer().append(Year).append("-").append("0").
                            append(Month + 1).append("-").append(Day).toString();
                }

            } else {
                if (Day < 10) {
                    days = new StringBuffer().append(Year).append("-").
                            append(Month + 1).append("-").append("0").append(Day).toString();
                } else {
                    days = new StringBuffer().append(Year).append("-").
                            append(Month + 1).append("-").append(Day).toString();
                }

            }
            date.setText(days);
        }, Year, Month, Day).show();
    }

    private void calcMoney(int i) {
        if (num.equals("0") && i == 0)
            return;
        if (isDot) {
            if (count < DOT_NUM) {
                count++;
                dotNum += i;
                money.setText(num + dotNum);
            }
        } else if (Integer.parseInt(num) < MAX_NUM) {
            if (num.equals("0"))
                num = "";
            num += i;
            money.setText(num + dotNum);
        }
    }

    public void doDelete() {  //删除上次输入
        if (isDot) {
            if (count > 0) {
                dotNum = dotNum.substring(0, dotNum.length() - 1);
                count--;
            }
            if (count == 0) {
                isDot = false;
                dotNum = ".00";
            }
            money.setText(num + dotNum);
        } else {
            if (num.length() > 0)
                num = num.substring(0, num.length() - 1);
            if (num.length() == 0)
                num = "0";
            money.setText(num + dotNum);
        }
    }

    public void doClear() {  //清空金额
        num = "0";
        count = 0;
        dotNum = ".00";
        isDot = false;
        money.setText("0.00");
    }

    protected void setTitle() {

    }

    public void doCommit() {  //提交账单
        final SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm:ss");
        final String crDate = days + sdf.format(new Date());
        if ((num + dotNum).equals("0.00")) {
            Toast.makeText(this, "唔姆，你还没输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Intent intent=getIntent();
            int position=intent.getIntExtra("position",0);
            intent.putExtra("position",position);
            intent.putExtra("textName",method.getText().toString());
            intent.putExtra("textPrice",money.getText().toString());
            intent.putExtra("pictureId",res);
            intent.putExtra("textWechat",WeChat.getText().toString());
            intent.putExtra("textInOutCome",cash.getText().toString());
            setResult(MainActivity.RESULT_CODE_ADD_DATA, intent);
            inputActivity.this.finish();
        }
    }
}


