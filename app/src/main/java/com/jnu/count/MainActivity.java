package com.jnu.count;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jnu.count.data.Count;
import com.jnu.count.data.DataBank;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private View drawerHeader;
    private DrawerLayout drawer;
    protected static final int UserMessage_CODE = 0;
    protected Context mContext;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView tOutcome;
    private TextView tIncome;
    private TextView tTotal;

    private ImageView drawerIv;
    private DataBank dataBank;

    public static final int RESULT_CODE_ADD_DATA = 996;
    private List<Count> countItems;
    private MyRecyclerViewAdapter recyclerViewAdapter;


    ActivityResultLauncher<Intent> launcherAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            int resultCode = result.getResultCode();

            if (resultCode == RESULT_CODE_ADD_DATA) {
                if (null == data) return;
                String textName = data.getStringExtra("textName");
                String textPrice = data.getStringExtra("textPrice");
                int pictureId = data.getIntExtra("pictureId",0);
                String textWechat = data.getStringExtra("textWechat");
                String textInOutCome = data.getStringExtra("textInOutCome");
                int position = data.getIntExtra("position", countItems.size());
                countItems.add(position, new Count(textName,textPrice,pictureId,textWechat,textInOutCome));
                dataBank.saveData();
                addBill(textInOutCome,textPrice);
                recyclerViewAdapter.notifyItemInserted(position);
            }
        }
    });

    ActivityResultLauncher<Intent> launcherEdit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            int resultCode = result.getResultCode();
            if (resultCode == RESULT_CODE_ADD_DATA) {
                if (null == data) return;
                String textName = data.getStringExtra("textName");
                String textPrice = data.getStringExtra("textPrice");
                int pictureId = data.getIntExtra("pictureId",0);
                String textWechat = data.getStringExtra("textWechat");
                String textInOutCome = data.getStringExtra("textInOutCome");
                int position = data.getIntExtra("position", countItems.size());

                editBill(countItems.get(position).getInoutCome(),textInOutCome,countItems.get(position).getPrice(),textPrice);
                countItems.get(position).setName(textName);
                countItems.get(position).setPrice(textPrice);
                countItems.get(position).setPictureId(pictureId);
                countItems.get(position).setWechat(textWechat);
                countItems.get(position).setInoutCome(textInOutCome);
                dataBank.saveData();
                recyclerViewAdapter.notifyItemChanged(position);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidget();
        initData();

        FloatingActionButton fabAdd = findViewById(R.id.floating_action_button_add);
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, inputActivity.class);
            intent.putExtra("position", countItems.size());
            launcherAdd.launch(intent);
        });

        RecyclerView mainRecycleView = findViewById(R.id.pager_type_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mainRecycleView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new MyRecyclerViewAdapter(countItems);
        mainRecycleView.setAdapter(recyclerViewAdapter);
    }


    public void initData() {
        dataBank = new DataBank(MainActivity.this);
        countItems = dataBank.loadData();
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
        private List<Count> countItems;

        public MyRecyclerViewAdapter(List<Count> countItems) {
            this.countItems = countItems;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.count_item_holder, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.MyViewHolder holder, int position) {
            holder.getImageView().setImageResource(countItems.get(position).getPictureId());
            holder.getTextViewName().setText(countItems.get(position).getName());
            holder.getTextViewPrice().setText(String.valueOf(countItems.get(position).getPrice()));
            holder.getTextViewWechat().setText(countItems.get(position).getWechat());
        }

        @Override
        public int getItemCount() {
            return countItems.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
            public static final int CONTEXT_MENU_ID_ADD = 1;
            public static final int CONTEXT_MENU_ID_UPDATE = CONTEXT_MENU_ID_ADD + 1;
            public static final int CONTEXT_MENU_ID_DELETE = CONTEXT_MENU_ID_ADD + 2;

            private final TextView textViewPrice;
            private final ImageView imageView;
            private final TextView textViewName;
            private final TextView textViewWechat;

            public MyViewHolder(View itemView) {
                super(itemView);

                this.textViewPrice = itemView.findViewById(R.id.text_view_count_item_price);
                this.imageView=itemView.findViewById(R.id.item_note_edit_iv);
                this.textViewName=itemView.findViewById(R.id.item_note_edit_tv);
                this.textViewWechat = itemView.findViewById(R.id.item_note_wechat);
                itemView.setOnCreateContextMenuListener(this);

            }

            public TextView getTextViewPrice() {
                return textViewPrice;
            }

            public TextView getTextViewName() {
                return textViewName;
            }

            public ImageView getImageView() {
                return imageView;
            }

            public TextView getTextViewWechat() {
                return textViewWechat;
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                MenuItem menuItemAdd = contextMenu.add(Menu.NONE, CONTEXT_MENU_ID_ADD, CONTEXT_MENU_ID_ADD, MainActivity.this.getResources().getString(R.string.string_menu_add));
                MenuItem menuItemEdit = contextMenu.add(Menu.NONE, CONTEXT_MENU_ID_UPDATE, CONTEXT_MENU_ID_UPDATE, MainActivity.this.getResources().getString(R.string.string_menu_edit));
                MenuItem menuItemDelete = contextMenu.add(Menu.NONE, CONTEXT_MENU_ID_DELETE, CONTEXT_MENU_ID_DELETE, MainActivity.this.getResources().getString(R.string.string_menu_delete));

                menuItemAdd.setOnMenuItemClickListener(this);
                menuItemEdit.setOnMenuItemClickListener(this);
                menuItemDelete.setOnMenuItemClickListener(this);

            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int position = getAdapterPosition();
                Intent intent;
                switch (menuItem.getItemId()) {
                    case CONTEXT_MENU_ID_ADD:
                        intent = new Intent(MainActivity.this, inputActivity.class);
                        intent.putExtra("position", position);
                        launcherAdd.launch(intent);
                        break;
                    case CONTEXT_MENU_ID_UPDATE:
                        intent = new Intent(MainActivity.this, inputActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("name", countItems.get(position).getName());
                        intent.putExtra("price", countItems.get(position).getPrice());
                        intent.putExtra("PictureID", countItems.get(position).getPictureId());
                        intent.putExtra("wechat", countItems.get(position).getWechat());
                        intent.putExtra("inoutCome", countItems.get(position).getInoutCome());
                        launcherEdit.launch(intent);
                        break;

                    case CONTEXT_MENU_ID_DELETE:
                        AlertDialog.Builder alertDB = new AlertDialog.Builder(MainActivity.this);
                        alertDB.setPositiveButton(MainActivity.this.getResources().getString(R.string.string_confirmation), (dialogInterface, i) -> {
                            deleteBill(countItems.get(position).getInoutCome(),countItems.get(position).getPrice());
                            countItems.remove(position);
                            dataBank.saveData();
                            MyRecyclerViewAdapter.this.notifyItemRemoved(position);
                        });
                        alertDB.setNegativeButton(MainActivity.this.getResources().getString(R.string.string_cancel), (dialogInterface, i) -> {

                        });
                        alertDB.setMessage(MainActivity.this.getResources().getString(R.string.string_confirm_delete) + countItems.get(position).getPrice() + "？");
                        alertDB.setTitle(MainActivity.this.getResources().getString(R.string.hint)).show();
                }
                return false;
            }
        }
    }

    protected void initWidget() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        drawer = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.main_nav_view);

        tOutcome = findViewById(R.id.outcome);
        tIncome = findViewById(R.id.income);
        tTotal = findViewById(R.id.total);

        //初始化Toolbar
        toolbar.setTitle("IUBill");
        //toolbar.setLogo(R.drawable.ic_launcher_foreground);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawerHeader = navigationView.inflateHeaderView(R.layout.activity_header_drawer);
        drawerIv = drawerHeader.findViewById(R.id.drawer_iv);

    }

    protected void addBill(String a,String b)
    {
        String text="支出";
        if(a.equals(text))
        {
            String i=tOutcome.getText().toString();
            String j=tTotal.getText().toString();
            Double value1=Double.parseDouble(i);
            Double value2=Double.parseDouble(j);
            Double value3=Double.parseDouble(b);
            value1=value1+value3;
            value2=value2-value3;
            tOutcome.setText(value1.toString());
            tTotal.setText(value2.toString());
        }
        else{
            String i=tIncome.getText().toString();
            String j=tTotal.getText().toString();
            Double value1=Double.parseDouble(i);
            Double value2=Double.parseDouble(j);
            Double value3=Double.parseDouble(b);
            value1=value1+value3;
            value2=value2+value3;
            tIncome.setText(value1.toString());
            tTotal.setText(value2.toString());
        }
    }

    protected void deleteBill(String a,String b)
    {
        String text="支出";
        if(a.equals(text))
        {
            String i=tOutcome.getText().toString();
            String j=tTotal.getText().toString();
            Double value1=Double.parseDouble(i);
            Double value2=Double.parseDouble(j);
            Double value3=Double.parseDouble(b);
            value1=value1-value3;
            value2=value2+value3;
            tOutcome.setText(value1.toString());
            tTotal.setText(value2.toString());
        }
        else{
            String i=tIncome.getText().toString();
            String j=tTotal.getText().toString();
            Double value1=Double.parseDouble(i);
            Double value2=Double.parseDouble(j);
            Double value3=Double.parseDouble(b);
            value1=value1-value3;
            value2=value2-value3;
            tIncome.setText(value1.toString());
            tTotal.setText(value2.toString());
        }
    }

    protected void editBill(String a,String c,String b,String d)
    {
        String text="支出";
        if(a.equals(text))
        {
            String i = tOutcome.getText().toString();
            String j = tTotal.getText().toString();
            Double value1 = Double.parseDouble(i);
            Double value2 = Double.parseDouble(j);
            Double value3 = Double.parseDouble(b);
            Double value4 = Double.parseDouble(d);
            if(c.equals(text)) {  //支出变支出
                if (value3 > value4) {
                    value3 -= value4;
                    value1 = value1 - value3;
                    value2 = value2 + value3;
                } else {
                    value4 -= value3;
                    value1 = value1 + value4;
                    value2 = value2 - value4;
                }
                tOutcome.setText(value1.toString());
                tTotal.setText(value2.toString());
            }
            else {  //支出转收入
                String k = tIncome.getText().toString();
                    value1 = value1 - value3;
                    value2=value2+value3;
                    value3=Double.parseDouble(k);
                    value3 = value3 + value4;
                    value2=value2+value4;
                tOutcome.setText(value1.toString());
                tTotal.setText(value2.toString());
                tOutcome.setText(value3.toString());
            }
        }
        else {
            String i = tIncome.getText().toString();
            String j = tTotal.getText().toString();
            Double value1 = Double.parseDouble(i);
            Double value2 = Double.parseDouble(j);
            Double value3 = Double.parseDouble(b);
            Double value4 = Double.parseDouble(d);
            if (c.equals(text)) {  //收入变支出
                String k = tOutcome.getText().toString();
                value1 = value1 - value3;
                value2 = value2 - value3;
                value3 = Double.parseDouble(k);
                value3 = value3 + value4;
                value2 = value2 - value4;
                tIncome.setText(value1.toString());
                tTotal.setText(value2.toString());
                tOutcome.setText(value3.toString());
            } else {  //收入到收入
                if (value3 > value4) {
                    value3 -= value4;
                    value1 = value1 - value3;
                    value2 = value2 - value3;
                } else {
                    value4 -= value3;
                    value1 = value1 + value4;
                    value2 = value2 + value4;
                }
                tIncome.setText(value1.toString());
                tTotal.setText(value2.toString());
            }
        }
    }
}