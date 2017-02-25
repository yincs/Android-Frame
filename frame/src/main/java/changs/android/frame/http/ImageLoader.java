package changs.android.frame.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.Target;

import changs.android.frame.app.AppClient;
import changs.android.frame.entity.action.Action1;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yincs on 2017/2/15.
 */

public abstract class ImageLoader {

    protected String url;

    @DrawableRes
    protected int placeholderRes;

    @DrawableRes
    protected int errorRes;

    protected int w = -1, h = -1;

    /**
     * 图片地址
     */
    public ImageLoader url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 设置图片高度、宽度
     */
    public ImageLoader wh(int w, int h) {
        this.w = w;
        this.h = h;
        return this;
    }
    /**
     * 加载中图片
     */
    public ImageLoader placeholderRes(@DrawableRes int placeholderRes) {
        this.placeholderRes = placeholderRes;
        return this;
    }

    /**
     * 加载失败图片
     */
    public ImageLoader errorRes(@DrawableRes int errorRes) {
        this.errorRes = errorRes;
        return this;
    }

    public static ImageLoader get() {
        return new GlideImageLoader(AppClient.getApplicationContext());
    }


    public static ImageLoader get(Context ctx) {
        return new GlideImageLoader(ctx);
    }


    /**
     * 加载进图片
     */
    public abstract void into(ImageView imageView);

    /**
     * 从回调中获取
     */
    public abstract void fetch(Action1<Bitmap> callBack);


    private static class GlideImageLoader extends ImageLoader {

        private final RequestManager requestManager;

        public GlideImageLoader(Context ctx) {
            requestManager = Glide.with(ctx);
        }


        @Override
        public void into(ImageView imageView) {
            final DrawableRequestBuilder<String> requestBuilder = requestManager
                    .load(url)
                    .centerCrop()
                    .placeholder(placeholderRes)
                    .error(errorRes)
                    .dontAnimate();
            if (w != -1 && h != -1) {
                requestBuilder.override(w, h).into(imageView);
            } else {
                requestBuilder.into(imageView);
            }

        }


        @Override
        public void fetch(final Action1<Bitmap> callBack) {
            Single
                    .create(new SingleOnSubscribe<Bitmap>() {
                        @Override
                        public void subscribe(SingleEmitter<Bitmap> singleEmitter) throws Exception {
                            final Bitmap bitmap = requestManager
                                    .load(url)
                                    .asBitmap()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                            singleEmitter.onSuccess(bitmap);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            callBack.call(bitmap);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            callBack.call(null);
                        }
                    });

        }
    }

}
