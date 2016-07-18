package bikurim.silverfix.com.bikurim.utils.interfaces;

import bikurim.silverfix.com.bikurim.adapters.holders.GenericViewHolder;

/**
 * Created by David on 10/07/2016.
 * A convenient interface for interacting between the CountDownManager
 * and the adapter
 *
 * @author David Lasry
 */
public interface HolderListener {
    void onRemoveTimeUpViewHolder(int pos, GenericViewHolder holder);
}
