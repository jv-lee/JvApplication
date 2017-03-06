package com.jv.daily.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class MultiTypeAdapter extends RecyclerView.Adapter<MultiTypeAdapter.ItemViewHolder> {
    public interface ItemInterface {
        //获取xml文件中 layout布局文件type
        int getLayout();

        //获取 xml文件中 绑定名字
        int getVariableId();
    }

    public Context context;

    public MultiTypeAdapter(Context context) {
        this.context = context;
    }

    private List<ItemInterface> items = new ArrayList<>();

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ItemViewHolder.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bindTo(items.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getLayout();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // 适配器 增删改查 封装方法  ----------------------------------

    //获取所有item
    public List<ItemInterface> getItems() {
        return items;
    }

    //获取当前item 下标位置
    public int findPosition(ItemInterface item) {
        return items.indexOf(item)+1;
    }

    public void setItem(ItemInterface item) {
        clearItem();
        addItem(item);
    }

    public void addItem(ItemInterface item) {
        items.add(item);
    }

    public void addItem(ItemInterface item, int index) {
        items.add(index, item);
    }

    public void addItems(List<ItemInterface> items) {
        this.items.addAll(items);
    }

    public int removeItem(ItemInterface item) {
        int position = findPosition(item);
        items.remove(item);
        return position;
    }

    public void clearItem() {
        items.clear();
    }


    /**
     * ViewDataBinding实现 ViewHolder
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        static ItemViewHolder create(ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
            return new ItemViewHolder(binding);
        }

        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(ItemInterface itemInterface) {
            binding.setVariable(itemInterface.getVariableId(), itemInterface);
            binding.executePendingBindings();
        }
    }

}
