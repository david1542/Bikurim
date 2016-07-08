package bikurim.silverfix.com.bikurim.adapters.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import bikurim.silverfix.com.bikurim.models.Family;

/**
 * Created by דודו on 07/07/2016.
 */
public abstract class GenericViewHolder extends RecyclerView.ViewHolder {

    protected Context context;
    protected View container;

    public GenericViewHolder(View item, Context context) {
        super(item);
        container = item;
        this.context = context;
    }

    public abstract void bindData(Family family);

    public abstract void reset();

    public View getView() {
        return container;
    }

    public Context getContext() {
        return context;
    }
}
