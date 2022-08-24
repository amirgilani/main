package info.ideveloper.altinzeit.Activitie;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.lang.ref.WeakReference;

import info.ideveloper.altinzeit.API.APIService;
import info.ideveloper.altinzeit.API.ApiClient;
import info.ideveloper.altinzeit.Adapter.FragmentViewPager.ViewPagerAdapter1;
import info.ideveloper.altinzeit.Base.BaseApplication;
import info.ideveloper.altinzeit.Fragment.Credit;
import info.ideveloper.altinzeit.Fragment.Credit_Sub.Credit_Main;
import info.ideveloper.altinzeit.Fragment.Factor;
import info.ideveloper.altinzeit.Fragment.Factor_Sub.FactorSearchResult;
import info.ideveloper.altinzeit.Fragment.Factor_Sub.Factor_Main;
import info.ideveloper.altinzeit.Fragment.InviteFriend;
import info.ideveloper.altinzeit.Fragment.InviteFriend_Sub.Invite_Main;
import info.ideveloper.altinzeit.Fragment.Notification;
import info.ideveloper.altinzeit.Fragment.Service;
import info.ideveloper.altinzeit.Fragment.Service_Sub.Add_Car;
import info.ideveloper.altinzeit.Fragment.Service_Sub.ProfileFragment;
import info.ideveloper.altinzeit.Fragment.Service_Sub.Search_Results;
import info.ideveloper.altinzeit.Fragment.Service_Sub.ServiceFactor;
import info.ideveloper.altinzeit.Fragment.Service_Sub.Service_Detail;
import info.ideveloper.altinzeit.Fragment.Service_Sub.Service_Main;
import info.ideveloper.altinzeit.Fragment.Service_Sub.Shop_Details;
import info.ideveloper.altinzeit.Fragment.Service_Sub.Show_Car_Fragment;
import info.ideveloper.altinzeit.Fragment.Service_Sub.Stores;
import info.ideveloper.altinzeit.Helper.HelperVariables;
import info.ideveloper.altinzeit.Helper.SharedPreferencesUtils;
import info.ideveloper.altinzeit.Models.FCMTokenResponse;
import info.ideveloper.altinzeit.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    public boolean isOnShowCar = false;
    public boolean isOnShopDetails = false;
    public boolean isOnStores = false;
    public ImageView btm;
    public boolean isOnAddCar = false;
    public boolean isOnSearchResult = false;
    public boolean isOnServiceDetails = false;
    public boolean isOnProfile = false;
    public boolean isOnServiceFactor = false;
    public boolean isOnFactorSearch = false;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    public BottomNavigationView bottomNavigation;
    //public ViewPager viewPager;
    public TextSwitcher title;
    public AppBarLayout appbar;
    MenuItem prevMenuItem = null;
    public String textToShow[] = {"دعوت دوستان           ", "فاکتورها              ", "سرویس                 ", "اعتبار                ", "اعلانات               ", "فروشگاه های طرف قرارداد", "خودروها                ", "ثبت خودرو              ", "اطلاعات فروشگاه        ", "نتایج جستجو            ", "ویرایش خودرو           ", "پروفایل                ", "اطلاعات سرویس          ", "اطلاعات فاکتور         "};
    //private FirebaseAnalytics mFirebaseAnalytics;
    private ViewPagerAdapter1 adapter1;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    //private Home homeFragment;
    //private Reserves reserveFragment;
    private LinearLayout setting, tog;
    private WeakReference<Service> serviceFragment;
    private WeakReference<Factor> factoreFragment;
    private WeakReference<InviteFriend> inviteFragment;
    private WeakReference<Credit> creditFragment;
    private WeakReference<Notification> notifFragment;
    public KProgressHUD kProgressHUD;

    private boolean fromBNV = false;

    private FirebaseMessaging firebaseMessaging;
    private FirebaseInstanceId firebaseInstanceId;

    private LinearLayout nav_home, nav_cars, nav_invite, nav_contactus, nav_notifications, nav_aboutus, nav_profile, nav_store;
    private Button nav_exit_btn;
    private LinearLayout selected_drawer_holder;
    private SharedPreferencesUtils sharedPreferencesUtils;
    //private Places placesFragment;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        gotonotifs = intent.getStringExtra("gotoNotifs") != null && !intent.getStringExtra("gotoNotifs").isEmpty();
        if (gotonotifs) {
            if (mHandler != null)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HelperVariables.viewPager.setCurrentItem(4);

                    }
                }, 300);
        }
    }


    boolean gotonotifs = false;
    private Runnable mRunnable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        gotonotifs = i.getStringExtra("gotoNotifs") != null && !i.getStringExtra("gotoNotifs").isEmpty();

        BaseApplication.service = ApiClient.getClient(this).create(APIService.class); //todo: getapplication context


        initViews();


        events();
    }


    int repeat = 0;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!sharedPreferencesUtils.getFCM_Token().isEmpty() && !sharedPreferencesUtils.getApiKey().isEmpty()) {
                Call<FCMTokenResponse> fcmSaveTokenResponseCall = BaseApplication.service.saveFCMToken(sharedPreferencesUtils.getApiKey(), sharedPreferencesUtils.getFCM_Token());
                fcmSaveTokenResponseCall.enqueue(new Callback<FCMTokenResponse>() {
                    @Override
                    public void onResponse(Call<FCMTokenResponse> call, Response<FCMTokenResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getErr().equals("not")) {
                                Log.d("SaveToken", "Done");
                            } else {
                                Log.d("SaveToken", response.body().getMessage());

                            }
                        } else {
                            Log.d("SaveToken", String.valueOf(response.code()));

                            if (repeat < 4) {
                                saveFcm();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMTokenResponse> call, Throwable t) {
                        Log.d("SaveToken", t.getMessage());

                        if (repeat < 4) {
                            saveFcm();

                        }
                    }
                });

            } else {
                if (mHandler != null)
                    mHandler.postDelayed(this, 5000);
            }
        }
    };


    private void saveFcm() {
        repeat += 1;


        if (mHandler != null) {
            mHandler.post(runnable);
        }
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        return status == ConnectionResult.SUCCESS;
    }

    public void events() {

        //todo: for every event analytic can be run
        //        Bundle bundle = new Bundle();
        //        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        //        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        //        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        //        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperVariables.viewPager.getCurrentItem() != 2) {
                    HelperVariables.viewPager.setCurrentItem(2, true);
                    Handler h = new Handler(getMainLooper());
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getServiceWeakReference().gotomain_profile(true);
                            } catch (Exception e) {
                                h.postDelayed(this, 100);
                            }
                        }
                    };
                    h.post(r);

                } else {
                    getServiceWeakReference().gotomain_profile(true);

                }

            }
        });

        tog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        nav_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
                if (selected_drawer_holder.getId() != nav_aboutus.getId()) {
                    selected_drawer_holder.setBackground(null);
                    nav_aboutus.setBackgroundResource(R.drawable.drawer_selected_background);
                    selected_drawer_holder = nav_aboutus;
                }
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("https://niakoil.ir/page/aboutus"));

                startActivity(browse);
            }
        });
        nav_exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoExitDialog(MainActivity.this);
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
            }
        });

        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
                if (selected_drawer_holder.getId() != nav_home.getId()) {
                    selected_drawer_holder.setBackground(null);
                    nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                    selected_drawer_holder = nav_home;
                }

                if (HelperVariables.viewPager.getCurrentItem() != 2)
                    HelperVariables.viewPager.setCurrentItem(2);
                else {
                    if (getServiceWeakReference() != null) {
                        getServiceWeakReference().gotomain(true);

                    }
                }
            }
        });

        nav_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
                if (selected_drawer_holder.getId() != nav_notifications.getId()) {
                    selected_drawer_holder.setBackground(null);
                    nav_notifications.setBackgroundResource(R.drawable.drawer_selected_background);
                    selected_drawer_holder = nav_notifications;
                }
                if (HelperVariables.viewPager.getCurrentItem() != 4)
                    HelperVariables.viewPager.setCurrentItem(4);
            }
        });

        nav_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);

                if (selected_drawer_holder.getId() != nav_cars.getId()) {
                    selected_drawer_holder.setBackground(null);
                    nav_cars.setBackgroundResource(R.drawable.drawer_selected_background);
                    selected_drawer_holder = nav_cars;
                }

                if (HelperVariables.viewPager.getCurrentItem() != 2) {
                    HelperVariables.viewPager.setCurrentItem(2, true);
                    Handler h = new Handler(getMainLooper());
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getServiceWeakReference().gotomain_cars(true);
                            } catch (Exception e) {
                                h.postDelayed(this, 100);
                            }
                        }
                    };
                    h.post(r);

                } else {
                    getServiceWeakReference().gotomain_cars(true);

                }

            }
        });
        nav_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);

                if (selected_drawer_holder.getId() != nav_contactus.getId()) {
                    selected_drawer_holder.setBackground(null);
                    nav_contactus.setBackgroundResource(R.drawable.drawer_selected_background);
                    selected_drawer_holder = nav_contactus;
                }
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("https://niakoil.ir/page/contactus"));

                startActivity(browse);
            }
        });
        nav_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);

                if (selected_drawer_holder.getId() != nav_profile.getId()) {
                    selected_drawer_holder.setBackground(null);
                    nav_profile.setBackgroundResource(R.drawable.drawer_selected_background);
                    selected_drawer_holder = nav_profile;
                }
                if (HelperVariables.viewPager.getCurrentItem() != 2) {
                    HelperVariables.viewPager.setCurrentItem(2, true);
                    Handler h = new Handler(getMainLooper());
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getServiceWeakReference().gotomain_profile(true);
                            } catch (Exception e) {
                                h.postDelayed(this, 100);
                            }
                        }
                    };
                    h.post(r);

                } else {
                    getServiceWeakReference().gotomain_profile(true);

                }
            }
        });
        nav_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);

                if (selected_drawer_holder.getId() != nav_invite.getId()) {
                    selected_drawer_holder.setBackground(null);
                    nav_invite.setBackgroundResource(R.drawable.drawer_selected_background);
                    selected_drawer_holder = nav_invite;
                }
                if (HelperVariables.viewPager.getCurrentItem() != 0) {
                    HelperVariables.viewPager.setCurrentItem(0);

                }
            }
        });
        nav_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);

                if (selected_drawer_holder.getId() != nav_store.getId()) {
                    selected_drawer_holder.setBackground(null);
                    nav_store.setBackgroundResource(R.drawable.drawer_selected_background);
                    selected_drawer_holder = nav_store;
                }

                if (HelperVariables.viewPager.getCurrentItem() != 2) {
                    HelperVariables.viewPager.setCurrentItem(2, true);
                    Handler h = new Handler(getMainLooper());
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getServiceWeakReference().gotomain_shop(true);
                            } catch (Exception e) {
                                h.postDelayed(this, 100);
                            }
                        }
                    };
                    h.post(r);

                } else {
                    getServiceWeakReference().gotomain_shop(true);

                }
            }
        });

        if (isGooglePlayServicesAvailable()) {

            firebaseMessaging = FirebaseMessaging.getInstance();
            firebaseInstanceId = FirebaseInstanceId.getInstance();


            if (sharedPreferencesUtils.getIsSubscribeOnGuestTopic()) {
                firebaseMessaging.unsubscribeFromTopic("guest").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            //msg = getString(R.string.msg_subscribe_failed);
                            return;
                        }
                        sharedPreferencesUtils.setIsSubscribeOnGuestTopic(false);
                        Log.d("FCM", "unsubscribe from guest");

                    }
                });
            }

            firebaseMessaging.subscribeToTopic("customer")
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            //msg = getString(R.string.msg_subscribe_failed);
                            return;
                        }

                        sharedPreferencesUtils.setIsSubscribeOnCustomerTopic(true);
                        Log.d("FCM", "subscribe to customer");

                    });

            firebaseInstanceId.getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                //Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            sharedPreferencesUtils.storeFcmToken(token);
                            //Log.d("FCM","token : "+token);


                        }
                    });
        } else {
            BaseApplication.showPosNegDialog(MainActivity.this);

        }
        saveFcm();
//        getAllMainPageData();
    }
//    int ii=0;
//    public void getAllMainPageData() {
//
//        if (utils.isOnline(this)){
//
//            kProgressHUD.show();
//                new getAll().execute();
//
//
//        }else {
//            showCustomDialog(MainActivity.this,"خطا در ارتباط با اینترنت",R.layout.alert_dialog);
//
//        }
//    }

//    private class getAll extends AsyncTask<String,String,Object> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            ii+=1;
//
//        }
//        @Override
//        protected MainDataModel doInBackground(String... strings) {
//
//            Call<CheckUserValidateResponse> singleCopon=BaseApplication.service.checkUserValidation(sharedPreferencesUtils.getApiKey());
//            Call<GetAllStaticDataResponse> relatedResponseCall=BaseApplication.service.getAllData();
//
//            MainDataModel mainDataModel=new MainDataModel();
//
//            try {
//                CheckUserValidateResponse allCoponResponse = singleCopon.execute().body();
//                GetAllStaticDataResponse relatedResponse=relatedResponseCall.execute().body();
//
//                mainDataModel.setCheckUserValidateResponse(allCoponResponse);
//                mainDataModel.setGetAllStaticDataResponse(relatedResponse);
//
//
//                if (relatedResponse!=null && relatedResponse.getErr()!=null && relatedResponse.getErr().equals("not")){
//                    HelperVariables.staticData=new GetAllStaticDataResponse();
//                    HelperVariables.staticData=relatedResponse;
//
//                }
//                if (allCoponResponse==null || allCoponResponse.getErr()==null || !allCoponResponse.getErr().equals("not")){
//                    sharedPreferencesUtils.setIsUserSignedIn(false);
//                    sharedPreferencesUtils.storeApiKey("");
//                    Intent intent = new Intent(MainActivity.this, SignUp.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.setAction(Intent.CATEGORY_HOME);
//                    startActivity(intent);
//                    finish();
//                    overridePendingTransition(R.anim.one1, R.anim.two1);
//                }
//
//
//            } catch (IOException e) {
//                if (ii==3) {
//
//                    new Handler(getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                                kProgressHUD.dismiss();
//
//                        }
//                    });
//                    showCustomDialog(MainActivity.this, "خطا در دریافت اطلاعات از سرور", R.layout.alert_dialog);
//                    return null;
//
//                }else {
//                    new Handler(getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            new getAll().execute();
//                        }
//                    });
//                }
//            }
//
//
//            return mainDataModel;
//
//        }
//        @Override
//        protected void onPostExecute(Object object) {
//            super.onPostExecute(object);
//
//            if (HelperVariables.staticData!=null && object!=null  && kProgressHUD!=null){
//                kProgressHUD.dismiss();
//
//            }else if (HelperVariables.staticData==null){
//                if (ii<3){
//                    new getAll().execute();
//                }else {
//                    kProgressHUD.dismiss();
//                }
//            }
//
//
//
//
//        }
//
//
//    }

    private void showCustomDialog(Context context, String title, int ResourceID) {
        Dialog withdrawDialog = new Dialog(context, R.style.AlertDialogStyle);

        withdrawDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        withdrawDialog.setContentView(ResourceID);
        View v = withdrawDialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        withdrawDialog.setCanceledOnTouchOutside(true);
        Button exit = v.findViewById(R.id.exit);
        TextView message = v.findViewById(R.id.message);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawDialog.dismiss();
            }
        });

        message.setText(title);


//        Resources r = context.getResources();
//        int px = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                10,
//                r.getDisplayMetrics()
//        );
//
//        CardView libase=withdrawDialog.findViewById(R.id.base);
//
//        libase.getLayoutParams().width= (nsv.getMeasuredWidth()-3*px);

        withdrawDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        withdrawDialog.show();

    }

    private void shoLogoutDialog(Context context) {
        Dialog withdrawDialog = new Dialog(context, R.style.AlertDialogStyle);

        withdrawDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        withdrawDialog.setContentView(R.layout.posneg_dialog);
        View v = withdrawDialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        withdrawDialog.setCanceledOnTouchOutside(true);
        Button pos = v.findViewById(R.id.pos);
        Button neg = v.findViewById(R.id.neg);

        pos.setText("بلی");
        neg.setText("خیر");
        TextView message = v.findViewById(R.id.message);
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesUtils.setIsUserSignedIn(false);
                sharedPreferencesUtils.storeApiKey("");
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setAction(Intent.CATEGORY_HOME);
                context.startActivity(intent);
                finish();
                overridePendingTransition(R.anim.one1, R.anim.two1);
            }
        });
        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawDialog.dismiss();
            }
        });

        message.setText("آیا مایل به خروج از حساب کاربری خود می باشید؟");


//        Resources r = context.getResources();
//        int px = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                10,
//                r.getDisplayMetrics()
//        );
//
//        CardView libase=withdrawDialog.findViewById(R.id.base);
//
//        libase.getLayoutParams().width= (nsv.getMeasuredWidth()-3*px);

        withdrawDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        withdrawDialog.show();

    }

    private void shoExitDialog(Context mainActivity) {
        Dialog withdrawDialog = new Dialog(mainActivity, R.style.AlertDialogStyle);

        withdrawDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        withdrawDialog.setContentView(R.layout.posneg_dialog);
        View v = withdrawDialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        withdrawDialog.setCanceledOnTouchOutside(true);
        Button pos = v.findViewById(R.id.pos);
        Button neg = v.findViewById(R.id.neg);

        pos.setText("بلی");
        neg.setText("خیر");
        TextView message = v.findViewById(R.id.message);
        message.setText("آیا مایل به خروج از برنامه می باشید؟");
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Dummy.class);
                mainActivity.startActivity(intent);
                finish();
            }
        });
        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawDialog.dismiss();
            }
        });

        withdrawDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        withdrawDialog.show();
    }

    private void setupViewPager() {
        adapter1 = new ViewPagerAdapter1(getSupportFragmentManager());
        HelperVariables.viewPager.setAdapter(adapter1);
        HelperVariables.viewPager.setOffscreenPageLimit(2);

    }

    public Service getServiceWeakReference() {

        serviceFragment = new WeakReference<>((Service) adapter1.getRegisteredFragment(2));
        return serviceFragment.get();


    }

    public Factor getFactorWeakReference() {


        factoreFragment = new WeakReference<>((Factor) adapter1.getRegisteredFragment(1));

        return factoreFragment.get();


    }

    public InviteFriend getInviteWeakReference() {

        inviteFragment = new WeakReference<>((InviteFriend) adapter1.getRegisteredFragment(0));

        return inviteFragment.get();


    }

    public Credit getCreditWeakReference() {

        creditFragment = new WeakReference<Credit>((Credit) adapter1.getRegisteredFragment(3));

        return creditFragment.get();


    }

    public Notification getNotificationWeakReference() {

        notifFragment = new WeakReference<>((Notification) adapter1.getRegisteredFragment(4));

        return notifFragment.get();


    }

    public void initViews() {
        registerReceiver(killer, new IntentFilter("kill-activity"));

        tog = findViewById(R.id.toggle);
        btm = findViewById(R.id.btm);
        mHandler = new Handler();
        mRunnable2 = new Runnable() {
            @Override
            public void run() {
                if (!kProgressHUD.isShowing()) {
                    kProgressHUD.show();

                } else {
                    if (mHandler != null)
                        mHandler.postDelayed(this, 100);
                }
            }
        };
        sharedPreferencesUtils = new SharedPreferencesUtils(this);
        kProgressHUD = new KProgressHUD(this);
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appbar = findViewById(R.id.appbar);
        title = toolbar.findViewById(R.id.pagetitle);
        setting = toolbar.findViewById(R.id.settings);
        title.setInAnimation(this, android.R.anim.slide_in_left);
        title.setOutAnimation(this, android.R.anim.slide_out_right);
        title.setFactory(() -> {
            // TODO Auto-generated method stub
            // create a TextView
            TextView t = new TextView(MainActivity.this);
            // set the gravity of text to top and center horizontal
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                t.setTextAppearance(MainActivity.this, R.style.TextAppearance_FontPath);
            } else {
                t.setTextAppearance(R.style.TextAppearance_FontPath);
            }
            t.setTextColor(Color.parseColor("#E1E1E1"));
            t.setTextSize(14);
            t.setLayoutParams(new FrameLayout.LayoutParams
                    (450, FrameLayout.LayoutParams.MATCH_PARENT));
            t.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);


            // add the textview to the parentLayout            // set displayed text size
            return t;
        });
        title.setCurrentText("آلتین زیت");

        drawer = findViewById(R.id.drawer_layout);
        if (Build.VERSION.SDK_INT < 17) {
            ViewCompat.setLayoutDirection(drawer, ViewCompat.LAYOUT_DIRECTION_RTL);

        }
        setSupportActionBar(toolbar);

//        toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//
//        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_toggle_icon);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        nav_invite = navigationView.findViewById(R.id.nav_invite);
        nav_exit_btn = navigationView.findViewById(R.id.nav_exit_btn);
        nav_home = navigationView.findViewById(R.id.nav_home);
        nav_cars = navigationView.findViewById(R.id.nav_cars);
        nav_contactus = navigationView.findViewById(R.id.nav_contactus);
        nav_aboutus = navigationView.findViewById(R.id.nav_aboutus);
        nav_profile = navigationView.findViewById(R.id.nav_profile);
        nav_store = navigationView.findViewById(R.id.nav_shop);
        nav_notifications = navigationView.findViewById(R.id.nav_notifications);
        selected_drawer_holder = nav_home;


        bottomNavigation = findViewById(R.id.bottomNavigation);
        if (Build.VERSION.SDK_INT < 17) {
            ViewCompat.setLayoutDirection(bottomNavigation, ViewCompat.LAYOUT_DIRECTION_LTR);

        }
        HelperVariables.viewPager = findViewById(R.id.viewpager);
        setupViewPager();
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {


            switch (item.getItemId()) {
                case R.id.navigation_service: {
                    if (getFactorWeakReference() != null) {
                        getFactorWeakReference().gotomain(false);
                    }
                    if (selected_drawer_holder.getId() != nav_home.getId()) {
                        selected_drawer_holder.setBackground(null);
                        nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                        selected_drawer_holder = nav_home;
                    }
                    if (HelperVariables.viewPager.getCurrentItem() != 2) {
                        //                        new Handler().post(new Runnable() {
                        //                            @Override
                        //                            public void run() {
                        fromBNV = true;
                        btm.setImageResource(R.drawable.bottomnav_service);
                        HelperVariables.viewPager.setCurrentItem(2);
                        //
                        //                            }
                        //                        });
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Service_Main notifications = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                                    if (notifications == null) {
                                        if (mHandler != null)
                                            mHandler.postDelayed(this, 200);
                                    } else {
                                        notifications.getAllMainPageData(false, false);
                                    }
                                } catch (Exception e) {
                                    if (mHandler != null) {

                                        mHandler.postDelayed(this, 200);
                                    }
                                }
                            }
                        };
                        if (mHandler != null)
                            mHandler.post(r);


                    } else {
                        if (getServiceWeakReference() != null) {
                            getServiceWeakReference().gotomain(true);

                        }

                    }


                    break;
                }
                case R.id.navigation_credit: {
                    if (getServiceWeakReference() != null) {
                        getServiceWeakReference().gotomain(false);
                    }
                    if (getFactorWeakReference() != null) {
                        getFactorWeakReference().gotomain(false);
                    }
                    if (HelperVariables.viewPager.getCurrentItem() != 3) {
                        fromBNV = true;
                        btm.setImageResource(R.drawable.bottomnav_credit);

                        HelperVariables.viewPager.setCurrentItem(3);
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Credit_Main notifications = (Credit_Main) getCreditWeakReference().getChildFragmentManager().findFragmentByTag("CreditMainFragment");

                                    if (notifications == null) {
                                        if (mHandler != null)
                                            mHandler.postDelayed(this, 200);
                                    } else {
                                        notifications.getCreditData(false, false);
                                    }
                                } catch (Exception e) {
                                    if (mHandler != null) {

                                        mHandler.postDelayed(this, 200);
                                    }
                                }

                            }
                        };
                        if (mHandler != null)
                            mHandler.post(r);
                    }


                    break;
                }
                case R.id.navigation_notif: {
                    if (selected_drawer_holder.getId() != nav_notifications.getId()) {
                        selected_drawer_holder.setBackground(null);
                        nav_notifications.setBackgroundResource(R.drawable.drawer_selected_background);
                        selected_drawer_holder = nav_notifications;
                    }
                    if (getServiceWeakReference() != null) {
                        getServiceWeakReference().gotomain(false);
                    }
                    if (getFactorWeakReference() != null) {
                        getFactorWeakReference().gotomain(false);
                    }
                    if (HelperVariables.viewPager.getCurrentItem() != 4) {
                        fromBNV = true;
                        btm.setImageResource(R.drawable.bottomnav_notifs);

                        HelperVariables.viewPager.setCurrentItem(4);
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Notification notifications = (Notification) getNotificationWeakReference();

                                    if (notifications == null) {
                                        if (mHandler != null)
                                            mHandler.postDelayed(this, 200);
                                    } else {
                                        notifications.getNotifications(false, false);
                                    }
                                } catch (Exception e) {
                                    if (mHandler != null) {

                                        mHandler.postDelayed(this, 200);
                                    }
                                }
                            }
                        };
                        if (mHandler != null)
                            mHandler.post(r);
                    }
                    break;
                }

                case R.id.navigation_invite: {
                    if (selected_drawer_holder.getId() != nav_invite.getId()) {
                        selected_drawer_holder.setBackground(null);
                        nav_invite.setBackgroundResource(R.drawable.drawer_selected_background);
                        selected_drawer_holder = nav_invite;
                    }
                    if (getServiceWeakReference() != null) {
                        getServiceWeakReference().gotomain(false);
                    }
                    if (getFactorWeakReference() != null) {
                        getFactorWeakReference().gotomain(false);
                    }
                    if (HelperVariables.viewPager.getCurrentItem() != 0) {
                        fromBNV = true;
                        btm.setImageResource(R.drawable.bottomnav_invite);

                        HelperVariables.viewPager.setCurrentItem(0);
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Invite_Main invite_Main = (Invite_Main) getInviteWeakReference().getChildFragmentManager().findFragmentByTag("InviteMainFragment");

                                    if (invite_Main == null) {
                                        mHandler.postDelayed(this, 200);
                                    } else {
                                        invite_Main.getInviteData(false, false);
                                    }
                                } catch (Exception e) {
                                    mHandler.postDelayed(this, 200);

                                }

                            }
                        };
                        if (mHandler != null)
                            mHandler.post(r);

                    } else {
                        if (getInviteWeakReference() != null) {
                            //getInviteWeakReference().gotomain(true);
                        }


                    }
                    break;
                }
                case R.id.navigation_factor: {
                    if (getServiceWeakReference() != null) {
                        getServiceWeakReference().gotomain(false);
                    }
                    if (HelperVariables.viewPager.getCurrentItem() != 1) {
                        fromBNV = true;
                        btm.setImageResource(R.drawable.bottomnav_factor);
                        HelperVariables.viewPager.setCurrentItem(1);

                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Factor_Main notifications = (Factor_Main) getFactorWeakReference().getChildFragmentManager().findFragmentByTag("FactorMainFragment");

                                    if (notifications == null) {
                                        if (mHandler != null)
                                            mHandler.postDelayed(this, 200);
                                    } else {
                                        notifications.getFactorData(false, false);
                                    }
                                } catch (Exception e) {
                                    if (mHandler != null) {

                                        mHandler.postDelayed(this, 200);
                                    }
                                }
                            }
                        };
                        if (mHandler != null)
                            mHandler.post(r);
                    } else {
                        if (getFactorWeakReference() != null) {
                            getFactorWeakReference().gotomain(true);

                        }

                    }
                    break;
                }
            }
            return false;
        });

        HelperVariables.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                appbar.setExpanded(true);
                showBottomNavigationView(bottomNavigation, btm);
                //showImageView(btm);


                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                //Log.d("page", "onPageSelected: " + position);
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                title.setText(textToShow[position]);
                prevMenuItem = bottomNavigation.getMenu().getItem(position);


                if (!fromBNV) {
                    if (getServiceWeakReference() != null) {
                        getServiceWeakReference().gotomain(false);
                    }
                    if (getFactorWeakReference() != null) {
                        getFactorWeakReference().gotomain(false);
                    }
                    switch (position) {
                        case 0: {
                            btm.setImageResource(R.drawable.bottomnav_invite);

                            if (selected_drawer_holder.getId() != nav_invite.getId()) {
                                selected_drawer_holder.setBackground(null);
                                nav_invite.setBackgroundResource(R.drawable.drawer_selected_background);
                                selected_drawer_holder = nav_invite;
                            }
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Invite_Main invite_Main = (Invite_Main) getInviteWeakReference().getChildFragmentManager().findFragmentByTag("InviteMainFragment");

                                        if (invite_Main == null) {
                                            mHandler.postDelayed(this, 200);
                                        } else {
                                            invite_Main.getInviteData(false, false);
                                        }
                                    } catch (Exception e) {
                                        mHandler.postDelayed(this, 200);

                                    }
                                }
                            };
                            if (mHandler != null)
                                mHandler.post(r);
                            break;
                        }
                        case 1: {
                            btm.setImageResource(R.drawable.bottomnav_factor);


                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Factor_Main notifications = (Factor_Main) getFactorWeakReference().getChildFragmentManager().findFragmentByTag("FactorMainFragment");

                                        if (notifications == null) {
                                            if (mHandler != null)
                                                mHandler.postDelayed(this, 200);
                                        } else {
                                            notifications.getFactorData(false, false);
                                        }
                                    } catch (Exception e) {
                                        if (mHandler != null) {

                                            mHandler.postDelayed(this, 200);
                                        }
                                    }
                                }
                            };
                            if (mHandler != null)
                                mHandler.post(r);
                            break;
                        }
                        case 2: {
                            btm.setImageResource(R.drawable.bottomnav_service);
                            if (selected_drawer_holder.getId() != nav_home.getId()) {
                                selected_drawer_holder.setBackground(null);
                                nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                                selected_drawer_holder = nav_home;
                            }
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Service_Main notifications = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                                        if (notifications == null) {
                                            if (mHandler != null)
                                                mHandler.postDelayed(this, 200);
                                        } else {
                                            notifications.getAllMainPageData(false, false);
                                        }
                                    } catch (Exception e) {
                                        if (mHandler != null) {

                                            mHandler.postDelayed(this, 200);
                                        }
                                    }
                                }
                            };
                            if (mHandler != null)
                                mHandler.post(r);
                            break;
                        }
                        case 3: {
                            btm.setImageResource(R.drawable.bottomnav_credit);

                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Credit_Main notifications = (Credit_Main) getCreditWeakReference().getChildFragmentManager().findFragmentByTag("CreditMainFragment");

                                        if (notifications == null) {
                                            if (mHandler != null)
                                                mHandler.postDelayed(this, 200);
                                        } else {
                                            notifications.getCreditData(false, false);
                                        }
                                    } catch (Exception e) {
                                        if (mHandler != null) {

                                            mHandler.postDelayed(this, 200);
                                        }
                                    }
                                }
                            };
                            if (mHandler != null)
                                mHandler.post(r);
                            break;
                        }
                        case 4: {
                            btm.setImageResource(R.drawable.bottomnav_notifs);
                            if (selected_drawer_holder.getId() != nav_notifications.getId()) {
                                selected_drawer_holder.setBackground(null);
                                nav_notifications.setBackgroundResource(R.drawable.drawer_selected_background);
                                selected_drawer_holder = nav_notifications;
                            }
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Notification notifications = (Notification) getNotificationWeakReference();

                                        if (notifications == null) {
                                            if (mHandler != null)
                                                mHandler.postDelayed(this, 200);
                                        } else {
                                            notifications.getNotifications(false, false);
                                        }
                                    } catch (Exception e) {
                                        if (mHandler != null) {

                                            mHandler.postDelayed(this, 200);
                                        }
                                    }
                                }
                            };
                            if (mHandler != null)
                                mHandler.post(r);
                            break;
                        }
                    }
                }
                fromBNV = false;
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        HelperVariables.viewPager.setCurrentItem(2);


        //PersianDateFormat persianDateFormat=new PersianDateFormat("Y/n/j");

        //DetailList<CalendarModel> calendarList=new ArrayList<>();


//        int monthdays=0;
//        for (int i = 1; i < 80; i++) {
//
//            if (persianDate.getMonthDays()<(i-monthdays)){
//                monthdays+=persianDate.getMonthDays();
//                if ((persianDate.getShMonth()+1)==13){
//                    persianDate.setShYear(persianDate.getShYear()+1);
//                }
//                persianDate.setShMonth((persianDate.getShMonth()+1)==13?1:(persianDate.getShMonth()+1));
//
//            }
//                persianDate.setShDay(i-monthdays);
//
//            //persianDate.addDay(i);
//            CalendarModel calendarModel=new CalendarModel();
//            if (persianDate.dayName().contains("پنج") || persianDate.dayName().contains("جمعه")){
//                calendarModel.setTatil(true);
//
//                calendarModel.setCalendarPrice(String.format("%,.0f", 110000.0f)+ " تومان ");
//
//            }else {
//                calendarModel.setTatil(false);
//                calendarModel.setCalendarPrice(String.format("%,.0f", 90000.0f)+ " تومان ");
//            }
//            calendarModel.setCalendarDate(persianDateFormat.format(persianDate));
//
//            calendarList.add(calendarModel);

//        }


        //calendarAdapter=new CalendarAdapter(calendarList,R.layout.calendar_item,this,this);
        //setupRecyclerView();

    }

    @Override
    public void onBackPressed() {
        boolean canexit = true;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isOnStores) {
                Stores calendar = (Stores) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceStoreFragment");
                Service_Main main = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                if (calendar != null && calendar.isVisible()) {
                    canexit = false;
                    if (main != null) {
                        isOnStores = false;
                        getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.one1, R.anim.two1)
                                .show(main)
                                .remove(calendar)
                                .commit();
                        title.setText(textToShow[2]);
                        if (selected_drawer_holder.getId() != nav_home.getId()) {
                            selected_drawer_holder.setBackground(null);
                            nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                            selected_drawer_holder = nav_home;
                        }

                    }
                }


            }
            if (isOnProfile) {
                ProfileFragment calendar = (ProfileFragment) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceProfileFragment");
                Service_Main main = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                if (calendar != null && calendar.isVisible()) {
                    canexit = false;
                    if (main != null) {
                        isOnProfile = false;
                        getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.one1, R.anim.two1)
                                .show(main)
                                .remove(calendar)
                                .commit();
                        title.setText(textToShow[2]);
                        if (selected_drawer_holder.getId() != nav_home.getId()) {
                            selected_drawer_holder.setBackground(null);
                            nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                            selected_drawer_holder = nav_home;
                        }
                    }
                }


            }
            if (isOnServiceDetails) {
                Service_Detail calendar = (Service_Detail) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceDetailFragment");
                Service_Main main = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                if (calendar != null && calendar.isVisible()) {
                    canexit = false;
                    if (main != null) {
                        isOnServiceDetails = false;
                        getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.one1, R.anim.two1)
                                .show(main)
                                .remove(calendar)
                                .commit();
                        title.setText(textToShow[2]);
                        if (selected_drawer_holder.getId() != nav_home.getId()) {
                            selected_drawer_holder.setBackground(null);
                            nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                            selected_drawer_holder = nav_home;
                        }
                    }
                }


            }
            if (isOnShowCar) {
                Show_Car_Fragment calendar = (Show_Car_Fragment) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceShowCarFragment");
                Service_Main main = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                if (calendar != null && calendar.isVisible()) {
                    canexit = false;
                    if (main != null) {
                        isOnShowCar = false;
                        getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.one1, R.anim.two1)
                                .show(main)
                                .remove(calendar)
                                .commit();
                        title.setText(textToShow[2]);
                        if (selected_drawer_holder.getId() != nav_home.getId()) {
                            selected_drawer_holder.setBackground(null);
                            nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                            selected_drawer_holder = nav_home;
                        }
                    }
                }


            }
            if (isOnSearchResult) {
                Search_Results calendar = (Search_Results) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceSearchResultFragment");
                Service_Main main = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                if (calendar != null && calendar.isVisible()) {
                    canexit = false;
                    if (main != null) {
                        isOnSearchResult = false;
                        getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.one1, R.anim.two1)
                                .show(main)
                                .remove(calendar)
                                .commit();
                        title.setText(textToShow[2]);
                        if (selected_drawer_holder.getId() != nav_home.getId()) {
                            selected_drawer_holder.setBackground(null);
                            nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                            selected_drawer_holder = nav_home;
                        }
                    }
                }


            }
            if (isOnFactorSearch) {
                FactorSearchResult calendar = (FactorSearchResult) getFactorWeakReference().getChildFragmentManager().findFragmentByTag("FactorSearchFragment");
                Factor_Main main = (Factor_Main) getFactorWeakReference().getChildFragmentManager().findFragmentByTag("FactorMainFragment");

                if (calendar != null && calendar.isVisible()) {
                    canexit = false;
                    if (main != null) {
                        isOnFactorSearch = false;
                        getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.one1, R.anim.two1)
                                .show(main)
                                .remove(calendar)
                                .commit();
                        title.setText(textToShow[1]);

                    }
                }


            }
            if (isOnShopDetails) {
                Shop_Details shopDetails = (Shop_Details) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceShopDetailFragment");
                Stores stores = (Stores) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceStoreFragment");
                Service_Main main = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                if (shopDetails != null && shopDetails.isVisible()) {
                    canexit = false;
                    if (stores != null) {
                        isOnShopDetails = false;
                        isOnStores = true;
                        getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.one1, R.anim.two1)
                                .show(stores)
                                .remove(shopDetails)
                                .commit();
                        title.setText(textToShow[5]);
                    } else {
                        if (main != null) {
                            isOnShopDetails = false;
                            getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                    setCustomAnimations(R.anim.one1, R.anim.two1)
                                    .show(main)
                                    .remove(shopDetails)
                                    .commit();
                            title.setText(textToShow[2]);
                            if (selected_drawer_holder.getId() != nav_home.getId()) {
                                selected_drawer_holder.setBackground(null);
                                nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                                selected_drawer_holder = nav_home;
                            }
                        }

                    }
                }

            }
            if (isOnServiceFactor) {
                ServiceFactor shopDetails = (ServiceFactor) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceFactorFragment");
                Service_Detail stores = (Service_Detail) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceDetailFragment");
                Service_Main main = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                if (shopDetails != null && shopDetails.isVisible()) {
                    canexit = false;
                    if (stores != null) {
                        isOnServiceFactor = false;
                        isOnServiceDetails = true;
                        getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.one1, R.anim.two1)
                                .show(stores)
                                .remove(shopDetails)
                                .commit();
                        title.setText(textToShow[12]);
                    } else {
                        if (main != null) {
                            isOnServiceDetails = false;
                            getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                    setCustomAnimations(R.anim.one1, R.anim.two1)
                                    .show(main)
                                    .remove(shopDetails)
                                    .commit();
                            title.setText(textToShow[2]);
                            if (selected_drawer_holder.getId() != nav_home.getId()) {
                                selected_drawer_holder.setBackground(null);
                                nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                                selected_drawer_holder = nav_home;
                            }
                        }

                    }
                }

            }
            if (isOnAddCar) {
                Add_Car add_car = (Add_Car) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceAddCarFragment");
                Show_Car_Fragment show_car_fragment = (Show_Car_Fragment) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceShowCarFragment");
                Service_Main main = (Service_Main) getServiceWeakReference().getChildFragmentManager().findFragmentByTag("ServiceMainFragment");

                canexit = false;
                if (show_car_fragment != null) {
                    isOnAddCar = false;
                    isOnShowCar = true;
                    getServiceWeakReference().getChildFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.one1, R.anim.two1)
                            .show(show_car_fragment)
                            .hide(add_car)
                            .remove(add_car)
                            .commit();
                    title.setText(textToShow[6]);
                } else {
                    if (main != null) {
                        isOnAddCar = false;
                        getServiceWeakReference().getChildFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.one1, R.anim.two1)
                                .show(main)
                                .hide(add_car)
                                .remove(add_car)
                                .commit();
                        title.setText(textToShow[2]);
                        if (selected_drawer_holder.getId() != nav_home.getId()) {
                            selected_drawer_holder.setBackground(null);
                            nav_home.setBackgroundResource(R.drawable.drawer_selected_background);
                            selected_drawer_holder = nav_home;
                        }
                    }

                }


            }


            if (canexit) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    startActivity(new Intent(this, Dummy.class));
                    finish();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast toast = Toast.makeText(this.getApplicationContext(), "برای خروج دکمه بازگشت را دوباره کلیک کنید", Toast.LENGTH_SHORT);
                View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                view.getBackground().setColorFilter(getResources().getColor(R.color.material_gray_400), PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.material_gray_900));

                toast.show();
                if (mHandler != null)
                    mHandler.postDelayed(mRunnable, 2000);
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_logout) {
//
//            BaseApplication.shoLogoutDialog(this);
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler.removeCallbacks(mRunnable2);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
//            mRunnable=null;
//            mRunnable2=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHandler == null) {
            mHandler = new Handler();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BaseApplication.getRefWatcher(this).watch(HelperVariables.viewPager);
//        BaseApplication.getRefWatcher(this).watch(adapter1);
        //BaseApplication.getRefWatcher(this).watch(mHandler);

        if (serviceFragment != null) {
            serviceFragment.clear();
        }
        if (factoreFragment != null) {
            factoreFragment.clear();
        }
        if (inviteFragment != null) {
            inviteFragment.clear();
        }
        if (notifFragment != null) {
            notifFragment.clear();
        }
        if (creditFragment != null) {
            creditFragment.clear();
        }

        kProgressHUD = null;
        toolbar = null;
        drawer = null;
        toggle = null;
        navigationView = null;
        bottomNavigation = null;
        title = null;
        appbar = null;
        prevMenuItem = null;
        textToShow = null;
        adapter1 = null;
        mRunnable = null;
        mRunnable2 = null;
        setting = null;
        creditFragment = null;
        serviceFragment = null;
        notifFragment = null;
        inviteFragment = null;
        factoreFragment = null;
        firebaseInstanceId = null;
        firebaseMessaging = null;

        HelperVariables.viewPager = null;
        HelperVariables.NotificationData = null;
        HelperVariables.mainDataModel = null;
        HelperVariables.AllCars = null;
        HelperVariables.CarDetails = null;
        HelperVariables.SelectedServiceDetail = null;
        HelperVariables.factorData = null;
        HelperVariables.factorSearchResultData = null;
        HelperVariables.serviceSearchResultData = null;
        HelperVariables.creditData = null;
        HelperVariables.InviteData = null;

        unregisterReceiver(killer);


    }

    private final BroadcastReceiver killer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    public void hideBottomNavigationView(BottomNavigationView view) {
        view.animate().translationY(view.getHeight());
    }

    public void showBottomNavigationView(BottomNavigationView view, ImageView imageView) {
        view.animate().translationY(0);
        imageView.animate().translationY(0);
    }

    private void hideImageView(ImageView view) {
        view.animate().translationY(view.getHeight());
    }


}
