package changs.android.frame.app;

/**
 * Created by yincs on 2017/2/14.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.util.HashMap;

import changs.android.utils.MyLog;
import changs.android.utils.Utils;

/**
 * 在放单例或全局对象的容器
 */
public final class AppClient {

    private static final String TAG = "AppClient";


    @SuppressLint("StaticFieldLeak")
    private static AppClient appClient;

    private final Config config;

    private AppClient(Config config) {
        this.config = config;
    }

    static AppClient get() {
        if (appClient == null) {
            throw new NullPointerException("必须先setup");
        }
        return appClient;
    }

    private final HashMap<Class<?>, Object> beans = new HashMap<>();

    /**
     * 常用全局工具类容器
     */
    private final HashMap<Class<?>, Object> sysBeans = new HashMap<>();


    public void initApp(Config config) {
        Utils.init(config.context);
    }

    public static Context getApplicationContext() {
        return get().config.context;
    }


    public static <T> T getBean(Class<T> tClass) {
        return get().getBean(get().beans, tClass);
    }

    public static boolean removeBean(Class tClass) {
        return get().removeBeans(get().beans, tClass);
    }

    public static <T> T getSysBean(Class<T> tClass) {
        return get().getBean(get().sysBeans, tClass);
    }

    public static boolean removeSysBean(Class tClass) {
        return get().removeBeans(get().sysBeans, tClass);
    }

    /**
     * 获取实例容器
     */
    public static HashMap<Class<?>, Object> getBeans() {
        return get().beans;
    }

    public static HashMap<Class<?>, Object> getSysBeans() {
        return get().sysBeans;
    }

    public static Config getConfig() {
        return get().config;
    }

    /**
     * @return true 是debug版本
     */
    public static boolean isDebug() {
        return get().config.debug;
    }


    /**
     * 获取类实例并添加到AppClient的容器中
     *
     * @param tClass 必须有可访问的无参构造器
     * @return 得到该class的实例
     */

    @SuppressWarnings("unchecked")
    private <T> T getBean(HashMap<Class<?>, Object> container, Class<T> tClass) {
        T t = (T) container.get(tClass);
        if (t == null) {
            try {
                t = tClass.newInstance();
                container.put(tClass, t);
            } catch (InstantiationException e) {
                throw new RuntimeException(tClass.getName() + "找不到该类的无参构造器");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(tClass.getName() + "该类的无参构造器不可访问");
            }
        }
        return t;
    }

    /**
     * 移除容器中的该实例
     *
     * @return true 移出成功
     */
    private boolean removeBeans(HashMap<Class<?>, Object> container, Class tClass) {
        if (!container.containsKey(tClass)) {
            MyLog.d(TAG, tClass.getName() + "对象不存在容器中");
            return true;
        }
        container.remove(tClass);
        return false;
    }


    @VisibleForTesting
    public static void test(Context applicationContext, boolean debug) {
        get().initApp(new Config(applicationContext).debug());
    }

    public static class Config {
        private final Context context;
        private boolean debug = false;
        private int logLevel = Log.ERROR;

        public Config(Context context) {
            this.context = context;
        }

        public Config debug() {
            this.debug = true;
            return this;
        }

        public Config logLevel(int logLevel) {
            this.logLevel = logLevel;
            return this;
        }


        public void setup() {
            if (AppClient.appClient != null) return;
            AppClient.appClient = new AppClient(this);
        }

        public int getLogLevel() {
            return logLevel;
        }

        public boolean isDebug() {
            return debug;
        }
    }
}
