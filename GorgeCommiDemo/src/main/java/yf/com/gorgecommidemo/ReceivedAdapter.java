package yf.com.gorgecommidemo;

/**
 * Created by 艾特不出先生 on 11/30 0030.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 弹出pop的adapter
 */
public class ReceivedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private static List<String> receivedMessageList;
   private Context context;


    public ReceivedAdapter(List<String> receivedMessageList, Context context) {
        this.receivedMessageList = receivedMessageList;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            //将position保存在itemView的Tag中,ViewHolder类中item数据赋值使用
            holder.itemView.setTag(position);
            ((ItemViewHolder) holder).initViews(position);
            // ((ItemViewHolder) holder).mItemLeftMain.setBackground(holder.itemView.getResources().getDrawable(R.drawable.item_left_main_slector));
        }
    }

    @Override
    public int getItemCount() {
        return receivedMessageList.size();
    }

    ////////////////////////////////////定义ItemViewHolder////////////////////////////////////////////////

    /**
     * 实例化MainActivity的item布局控件
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.received_message)
        TextView receivedMessage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }


        /**
         * 初始化控件
         */
        public void initViews(int position) {
            receivedMessage.setText(receivedMessageList.get(position));

        }


    }
}