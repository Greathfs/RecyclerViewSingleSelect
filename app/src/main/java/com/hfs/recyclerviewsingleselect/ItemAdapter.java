package com.hfs.recyclerviewsingleselect;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * @author HuangFusheng
 * @date 2019-11-11
 * description 适配器
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<ItemBean> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    private int mSelectedPos = -1;

    private RecyclerView mRv;

    public ItemAdapter(List<ItemBean> datas, Context context, RecyclerView rv) {
        mDatas = datas;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mRv = rv;

        //找到默认选中的position
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).isSelected()) {
                mSelectedPos = i;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.ivSelect.setSelected(mDatas.get(position).isSelected());
        holder.tvName.setText(mDatas.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewHolder viewHolder = (ViewHolder) mRv.findViewHolderForLayoutPosition(mSelectedPos);
                //表示还在屏幕里
                if (viewHolder != null) {
                    viewHolder.ivSelect.setSelected(false);
                } else {
                    notifyItemChanged(mSelectedPos);
                }
                //改变数据状态
                mDatas.get(mSelectedPos).setSelected(false);
                //设置新Item的勾选状态
                mSelectedPos = position;
                mDatas.get(mSelectedPos).setSelected(true);
                holder.ivSelect.setSelected(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != mDatas ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivSelect;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSelect = (ImageView) itemView.findViewById(R.id.iv_item_select);
            tvName = (TextView) itemView.findViewById(R.id.tv_item_name);
        }
    }
}
