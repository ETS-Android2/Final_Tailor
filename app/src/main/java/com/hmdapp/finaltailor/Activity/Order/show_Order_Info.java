package com.hmdapp.finaltailor.Activity.Order;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Person;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
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

import com.google.api.Control;
import com.hmdapp.finaltailor.Activity.Customer.Show_Model_Activity;
import com.hmdapp.finaltailor.Activity.Report_Dashbord.EveryRemainderActivity;
import com.hmdapp.finaltailor.Activity.Report_Dashbord.ReportActivity;
import com.hmdapp.finaltailor.Models.Cloth;
import com.hmdapp.finaltailor.Models.Customer;
import com.hmdapp.finaltailor.Models.Order;
import com.hmdapp.finaltailor.Models.Payment;
import com.hmdapp.finaltailor.R;
import com.hmdapp.finaltailor.Utlity.Tools;
import com.hmdapp.finaltailor.database.DB_Acsess;
import com.hmdapp.finaltailor.database.LocalData;


public class show_Order_Info extends AppCompatActivity {

    private TextView customerName, customerJob, deliveryDate, tvColor,
            tvCount, tvPrice, tvPayment, tvRemainder, tvRegDate;
    private CheckBox checkState, checkIsExist;

    Order order;
    Cloth cloth;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_order_info);
        initToolbar();
        setUpViews();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_payment);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initViews() {
        DB_Acsess db_acsess = DB_Acsess.getInstans(this);
        db_acsess.open();
        int id = getIntent().getIntExtra("id", 1);


        order = db_acsess.getOrder(id);
        cloth = order.getCloth();
        customer = cloth.getCustomer();

        Log.d("order_id", "id:  " + id);
        Payment payment = null;
        try {
            payment = db_acsess.getPayment(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("Payment", payment.toString());

        getSupportActionBar().setTitle(customer.getName());

        customerName.setText(order.getCloth().getCustomer().getName());
        customerJob.setText(customer.getJob());
        //Toast.makeText(show_Order_Info.this,""+customer.getPhone(),Toast.LENGTH_LONG).show();

        deliveryDate.setText(order.getDeliverDate());
        tvColor.setText(order.getColor());
        tvCount.setText(order.getCount() + "");
        tvPrice.setText(order.getPrice() + "");
        try {
            tvPayment.setText(payment.getPish_pardakht() + "");
            tvRemainder.setText(payment.getReminder() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvRegDate.setText(order.getOrder_Date());
        int state = order.getCom_state();
        if (state == 0) {
            checkState.setChecked(false);
        } else {
            checkState.setChecked(true);
        }
        int stateIsExist = order.getIsExist();
        if (stateIsExist == 0) {
            checkIsExist.setChecked(false);
        } else {
            checkIsExist.setChecked(true);
        }
        checkState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DB_Acsess db_acsess = DB_Acsess.getInstans(show_Order_Info.this);
                db_acsess.open();
                int id = getIntent().getIntExtra("id", 1);
                Order order = new Order();
                Payment payment = new Payment();

                order.setId(id);
                payment.setOrder(order);
                if (isChecked) {
                    order.setCom_state(1);

                    db_acsess.updateTask(order);
                    LocalData localData = new LocalData(getApplicationContext());
                    if (localData.get_customer_confirm()) {
                        onsend_sms();
                    }

                    // Toast.makeText(show_Order_Info.this, "True = 1", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(show_Order_Info.this, Tasks_Activity.class));
//                    finish();
                } else {
                    order.setCom_state(0);

                    db_acsess.updateTask(order);
//                    startActivity(new Intent(show_Order_Info.this, Tasks_Activity.class));
//                    finish();
                    //  Toast.makeText(show_Order_Info.this,"false = 0", Toast.LENGTH_LONG).show();
                }

            }
        });

        checkIsExist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DB_Acsess db_acsess = DB_Acsess.getInstans(show_Order_Info.this);
                db_acsess.open();
                int id = getIntent().getIntExtra("id", 1);
                Order order = new Order();
                order.setId(id);
                if (isChecked) {
                    order.setIsExist(1);
                    db_acsess.updateTaskClothState(order);
                    // Toast.makeText(show_Order_Info.this, "True = 1", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(show_Order_Info.this, Tasks_Activity.class));
                    finish();
                } else {
                    order.setIsExist(0);
                    db_acsess.updateTaskClothState(order);
                    startActivity(new Intent(show_Order_Info.this, Tasks_Activity.class));
                    finish();
                    //  Toast.makeText(show_Order_Info.this,"false = 0", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void setUpViews() {
        customerName = findViewById(R.id.task_customer_name_payment_top);
        customerJob = findViewById(R.id.task_job_payment);
        deliveryDate = findViewById(R.id.task_date_payment);
        tvColor = findViewById(R.id.task_color_payment);
        tvCount = findViewById(R.id.task_count_payment);
        tvPrice = findViewById(R.id.task_price_state);
        tvPayment = findViewById(R.id.task_payment_payment);
        tvRemainder = findViewById(R.id.task_remainder_pay);
        tvRegDate = findViewById(R.id.task_reg_date_payment);
        checkState = findViewById(R.id.task_check_state);
        checkIsExist = findViewById(R.id.task_check_is_exist);
        tvPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = Integer.parseInt(tvPayment.getText().toString());
                if (x == 0) {

                } else {

                    Intent intent = new Intent(getApplicationContext(), Show_Payment_of_order.class);
                    int remainder = Integer.parseInt(tvRemainder.getText().toString());
                    intent.putExtra("reminder", remainder);
                    int id = getIntent().getIntExtra("id", 0);
                    intent.putExtra("order_id", id);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            if (getIntent().getIntExtra("state", 0) == 0) {
                startActivity(new Intent(this, Tasks_Activity.class));
                finish();
            } else if (getIntent().getIntExtra("state", 0) == 1) {

                Intent intent = new Intent(this, ReportActivity.class);

                intent.putExtra("myKey", getIntent().getStringExtra("myKey"));
                startActivity(intent);
                finish();
            } else if (getIntent().getIntExtra("state", 0) == 2) {

                Intent intent = new Intent(this, EveryRemainderActivity.class);
                intent.putExtra("id", getIntent().getIntExtra("cu_id", 1));

                startActivity(intent);
                finish();
            }

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
                DB_Acsess db_acsess = DB_Acsess.getInstans(show_Order_Info.this);
                db_acsess.open();
                int taskId = getIntent().getIntExtra("id", 1);
                //Toast.makeText(show_Order_Info.this, "ID : "+taskId, Toast.LENGTH_SHORT).show();


                if (db_acsess.deleteTask(taskId) == true) {
                    Toast.makeText(show_Order_Info.this, "تسک موفقانه حذف شد", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(show_Order_Info.this, Tasks_Activity.class));
                    finish();
                } else {
                    Toast.makeText(show_Order_Info.this, "تسک حذف نشد", Toast.LENGTH_SHORT).show();
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

        TextView text_cu = dialog.findViewById(R.id.text_customer);

        String cuName = getIntent().getStringExtra("data");

        text_cu.setText(cuName);
        edPayment = dialog.findViewById(R.id.txt_payment_after_work);


        dialog.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB_Acsess db_acsess = DB_Acsess.getInstans(show_Order_Info.this);
                db_acsess.open();


                float remainder = Float.parseFloat(tvRemainder.getText().toString());


                int value = Integer.parseInt(edPayment.getText().toString());


                //Toast.makeText(getApplicationContext(), "Price : "+price+" , Payment : "+payment +" , Value : "+value, Toast.LENGTH_SHORT).show();

                if (value - remainder <= 0) {

                    int taskId = getIntent().getIntExtra("id", 1);
                    Order order = new Order();
                    Payment payment_ = new Payment();

                    order.setId(taskId);

                    payment_.setOrder(order);
                    payment_.setAmount(value);

                    long time = System.currentTimeMillis();
                    String regDate = Tools.getFormattedDateSimple(time);
                    payment_.setDate(regDate);
                    db_acsess.Add_Rasid(payment_);

                    // Toast.makeText(getApplicationContext(), "Hi "+regDate, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                    startActivity(new Intent(show_Order_Info.this, Tasks_Activity.class));
                    finish();
                } else if (remainder == 0) {
                    Toast.makeText(getApplicationContext(), "پرداخت از قبل تکمیل شده است " + "\n   دکمه لغو را فشار دهید   ", Toast.LENGTH_SHORT).show();
                } else {

                    //Toast.makeText(show_Order_Info.this,"Remainder : "+tvRemainder.getText().toString() + " Price : "+tvPrice.getText().toString(),Toast.LENGTH_LONG).show();

                    // float price = Float.parseFloat(tvPrice.getText().toString());
                    Toast.makeText(getApplicationContext(), "مقدار پول باقی مانده را اشتباه وارد نمودید ", Toast.LENGTH_SHORT).show();

                }

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void caa(View view) {

        String phon = getIntent().getStringExtra("phone");

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phon));//change the number

        try {
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException ex) {

            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
        }
    }

    public void onsend_sms() {
        LocalData localData = new LocalData(this);
        String phon = getIntent().getStringExtra("phone");

//        Uri uri = Uri.parse("smsto:0800000123");
        Intent it = new Intent(Intent.ACTION_SEND);
        it.setType("text/plain");

        it.putExtra(Intent.EXTRA_TEXT, localData.get_cutomer_text());
        it.putExtra("address", phon);
        startActivity(it);

//        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//        smsIntent.setType("vnd.android-dir/mms-sms");
//        smsIntent.putExtra("address",phon);
//        smsIntent.putExtra("sms_body",localData.get_cutomer_text());
//        startActivity(smsIntent);

        //Get the SmsManager instance and call the sendTextMessage method to send message
//        SmsManager sms=SmsManager.getDefault();
//        sms.sendTextMessage(phon, null, localData.get_cutomer_text(), null,null);

        Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                Toast.LENGTH_LONG).show();
    }

    public void onsendsms(View view) {


        String phon = getIntent().getStringExtra("phone");


        Intent it = new Intent(Intent.ACTION_SEND);
        it.setType("text/plain");


        it.putExtra("address", phon);
        startActivity(it);


    }

    public void onproperty(View view) {

        Intent intent = new Intent(getApplicationContext(), Show_Model_Activity.class);

        intent.putExtra("id_cl", cloth.getId());
        intent.putExtra("id_cu", customer.getId());
        intent.putExtra("cu_name", customer.getName());
        intent.putExtra("date", cloth.getDes());
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {


        if (getIntent().getIntExtra("state", 0) == 0) {
            startActivity(new Intent(this, Tasks_Activity.class));
            finish();
        } else if (getIntent().getIntExtra("state", 0) == 1) {

            Intent intent = new Intent(this, ReportActivity.class);

            intent.putExtra("myKey", getIntent().getStringExtra("myKey"));
            startActivity(intent);
            finish();
        } else if (getIntent().getIntExtra("state", 0) == 2) {

            Intent intent = new Intent(this, EveryRemainderActivity.class);

            intent.putExtra("myKey", getIntent().getStringExtra("myKey"));

            intent.putExtra("id", getIntent().getIntExtra("cu_id", 1));

            startActivity(intent);
            finish();
        }

    }
}
