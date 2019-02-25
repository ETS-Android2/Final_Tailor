package com.hmdapp.finaltailor.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hmdapp.finaltailor.Models.Customer;
import com.hmdapp.finaltailor.Models.Payment;
import com.hmdapp.finaltailor.Models.Task;
import com.hmdapp.finaltailor.R;
import com.hmdapp.finaltailor.Utlity.Tools;
import com.hmdapp.finaltailor.database.DB_Acsess;


public class PaymentActivity extends AppCompatActivity {

    private TextView customerName, customerJob, deliveryDate,tvColor,
            tvCount,tvPrice,tvPayment,tvRemainder,tvRegDate;
    private CheckBox checkState,checkIsExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initToolbar();
        setUpViews();
        initViews();
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_payment);
        String name = getIntent().getStringExtra("name");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.purple_600);
    }

    private void initViews(){
        DB_Acsess db_acsess = DB_Acsess.getInstans(this);
        db_acsess.open();
        int id = getIntent().getIntExtra("id", 1);
        int customerId = getIntent().getIntExtra("cu_id", 1);
        String deliverCuDate = getIntent().getStringExtra("deliver_date");
        int state = getIntent().getIntExtra("state",0);
        int stateIsExist = getIntent().getIntExtra("is_exist",0);

        Customer customer = db_acsess.getCustomer(customerId);

        customerName.setText(customer.getName());
        customerJob.setText(customer.getJob());
        //Toast.makeText(PaymentActivity.this,""+customer.getPhone(),Toast.LENGTH_LONG).show();

        deliveryDate.setText(deliverCuDate);
        tvColor.setText(getIntent().getStringExtra("color"));
        tvCount.setText(getIntent().getIntExtra("count",1)+"");
        tvPrice.setText((int) getIntent().getFloatExtra("price",1)+"");
        tvPayment.setText((int) getIntent().getFloatExtra("payment", 1)+"");
        tvRemainder.setText((int) getIntent().getFloatExtra("remainder", 1)+"");
        tvRegDate.setText(getIntent().getStringExtra("reg_date"));

        if (state == 0){
            checkState.setChecked(false);
        }else{
            checkState.setChecked(true);
        }
        if (stateIsExist == 0){
            checkIsExist.setChecked(false);
        }else{
            checkIsExist.setChecked(true);
        }
       checkState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DB_Acsess db_acsess = DB_Acsess.getInstans(PaymentActivity.this);
                db_acsess.open();
                int id = getIntent().getIntExtra("id", 1);
                Task task = new Task();
                Payment payment = new Payment();

                task.setId(id);
                payment.setTaskID(id);
                if(isChecked){
                    task.setState(1);
                    payment.setState(1);
                    db_acsess.updateTask(PaymentActivity.this,task,payment);
                   // Toast.makeText(PaymentActivity.this, "True = 1", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PaymentActivity.this,Tasks_Activity.class));
                    finish();
                }else {
                    task.setState(0);
                    payment.setState(0);
                    db_acsess.updateTask(PaymentActivity.this,task,payment);
                    startActivity(new Intent(PaymentActivity.this,Tasks_Activity.class));
                    finish();
                  //  Toast.makeText(PaymentActivity.this,"false = 0", Toast.LENGTH_LONG).show();
                }

            }
        });

        checkIsExist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DB_Acsess db_acsess = DB_Acsess.getInstans(PaymentActivity.this);
                db_acsess.open();
                int id = getIntent().getIntExtra("id", 1);
                Task task = new Task();
                task.setId(id);
                if(isChecked){
                    task.setIsExist(1);
                    db_acsess.updateTaskClothState(PaymentActivity.this,task);
                    // Toast.makeText(PaymentActivity.this, "True = 1", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PaymentActivity.this,Tasks_Activity.class));
                    finish();
                }else {
                    task.setIsExist(0);
                    db_acsess.updateTaskClothState(PaymentActivity.this,task);
                    startActivity(new Intent(PaymentActivity.this,Tasks_Activity.class));
                    finish();
                    //  Toast.makeText(PaymentActivity.this,"false = 0", Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    private void setUpViews(){
        customerName = findViewById(R.id.task_customer_name_payment_top);
        customerJob = findViewById(R.id.task_job_payment);
        deliveryDate = findViewById(R.id.task_date_payment);
        tvColor = findViewById(R.id.task_color_payment);
        tvCount = findViewById(R.id.task_count_payment);
        tvPrice = findViewById(R.id.task_price_state);
        tvPayment = findViewById(R.id.task_payment_payment);
        tvRemainder = findViewById(R.id.task_remainder_pay);
        tvRegDate = findViewById(R.id.task_reg_date_payment);
        checkState =  findViewById(R.id.task_check_state);
        checkIsExist =  findViewById(R.id.task_check_is_exist);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        } else if (item.getItemId() == R.id.action_pay_task) {

            customerPayment();

           // Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_Delete_task) {
            showAlertDialog();
          //  Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }


    private void showAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("آیا میخواهید تسک را پاک کنید ؟");
        builder.setPositiveButton("بلی", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DB_Acsess db_acsess = DB_Acsess.getInstans(PaymentActivity.this);
                db_acsess.open();
                int taskId = getIntent().getIntExtra("id", 1);
                //Toast.makeText(PaymentActivity.this, "ID : "+taskId, Toast.LENGTH_SHORT).show();


                if(db_acsess.deleteTask(taskId) == true){
                    Toast.makeText(PaymentActivity.this, "تسک موفقانه حذف شد", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentActivity.this, Tasks_Activity.class));
                    finish();
                }else{
                    Toast.makeText(PaymentActivity.this, "تسک حذف نشد", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("نخیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        builder.show();
    }

    private void customerPayment() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_payment);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final EditText edPayment;

        edPayment = dialog.findViewById(R.id.txt_payment_after_work);




        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB_Acsess db_acsess = DB_Acsess.getInstans(PaymentActivity.this);
                db_acsess.open();

               // String cuName = getIntent().getStringExtra("name");

                float remainder = Float.parseFloat(tvRemainder.getText().toString());
                float price = Float.parseFloat(tvPrice.getText().toString());
                float payment = Float.parseFloat(tvPayment.getText().toString());
                float valuePayment = price;

                float value = Float.parseFloat(edPayment.getText().toString());


                  //Toast.makeText(getApplicationContext(), "Price : "+price+" , Payment : "+payment +" , Value : "+value, Toast.LENGTH_SHORT).show();
               if (remainder == value) {

                   int taskId = getIntent().getIntExtra("id", 1);
                   Task task = new Task();
                   Payment payment_ = new Payment();
                   Customer customer = new Customer();
                   task.setId(taskId);
                   task.setRemainder(0);
                   task.setPayment(valuePayment);

                   payment_.setTaskID(taskId);
                   payment_.setRemainder(0);
                   payment_.setPayment(valuePayment);

                   db_acsess.updateTaskPayment(PaymentActivity.this,task,payment_);

                   //  Toast.makeText(getApplicationContext(), "Hi "+regDate, Toast.LENGTH_SHORT).show();
                   dialog.dismiss();

                   startActivity(new Intent(PaymentActivity.this,Tasks_Activity.class));
                   finish();
                }else if(remainder == 0 ){
                   Toast.makeText(getApplicationContext(), "پرداخت از قبل تکمیل شده است "+"\n   دکمه لغو را فشار دهید   ", Toast.LENGTH_SHORT).show();
               }

                else {

                   //Toast.makeText(PaymentActivity.this,"Remainder : "+tvRemainder.getText().toString() + " Price : "+tvPrice.getText().toString(),Toast.LENGTH_LONG).show();

                  // float price = Float.parseFloat(tvPrice.getText().toString());
                   Toast.makeText(getApplicationContext(), "مقدار پول باقی مانده را اشتباه وارد نمودید ", Toast.LENGTH_SHORT).show();

                }

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
