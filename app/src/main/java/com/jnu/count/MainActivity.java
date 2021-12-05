package com.jnu.count;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.count.data.Count;
import com.jnu.count.data.DataBank;

import java.util.List;

public class MainActivity extends AppCompatActivity {

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
                double price = data.getDoubleExtra("price", 0);
                int position = data.getIntExtra("position", countItems.size());
                countItems.add(position, new Count(price));
                dataBank.saveData();
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
                double price = data.getDoubleExtra("price", 0);
                int position = data.getIntExtra("posit.setPrice(price);ion", countItems.size());
                countItems.get(position).setPrice(price);
                dataBank.saveData();
                recyclerViewAdapter.notifyItemChanged(position);
            }
        }
    });
    private DataBank dataBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawerLayout=findViewById(R.id.drawerLayout);
        //给按钮添加一个监听器
        findViewById(R.id.top_view_left_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开侧滑菜单
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        initData();

        FloatingActionButton fabAdd = findViewById(R.id.floating_action_button_add);
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, inputActivity.class);
            intent.putExtra("position", countItems.size());
            launcherAdd.launch(intent);
        });

        RecyclerView mainRecycleView = findViewById(R.id.recycle_view_count);
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
            holder.getTextViewPrice().setText(String.valueOf(countItems.get(position).getPrice()));
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

            public MyViewHolder(View itemView) {
                super(itemView);

                this.textViewPrice = itemView.findViewById(R.id.text_view_count_item_price);

                itemView.setOnCreateContextMenuListener(this);

            }

            public TextView getTextViewPrice() {
                return textViewPrice;
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
                        intent.putExtra("price", countItems.get(position).getPrice());
                        launcherEdit.launch(intent);
                        break;

                    case CONTEXT_MENU_ID_DELETE:
                        AlertDialog.Builder alertDB = new AlertDialog.Builder(MainActivity.this);
                        alertDB.setPositiveButton(MainActivity.this.getResources().getString(R.string.string_confirmation), (dialogInterface, i) -> {
                            countItems.remove(position);
                            dataBank.saveData();
                            MyRecyclerViewAdapter.this.notifyItemRemoved(position);
                        });
                        alertDB.setNegativeButton(MainActivity.this.getResources().getString(R.string.string_cancel), (dialogInterface, i) -> {

                        });
                        alertDB.setMessage(MainActivity.this.getResources().getString(R.string.string_confirm_delete) + countItems.get(position).getPrice() + "？");
                        alertDB.setTitle(MainActivity.this.getResources().getString(R.string.hint)).show();
                        break;
                }
                return false;
            }
        }
    }

}