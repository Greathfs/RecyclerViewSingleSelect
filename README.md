# RecyclerViewSingleSelect

# 前言
我们经常会遇到列表中需要单选这个需求,之前的做法是:在我们的数据源`ItemBean`里增加一个`boolean` 类型`isSelected`字段，并在`Adapter`里根据这个字段来判定选中状态。每次选中一个新的`item`时，改变数据源里的i`sSelected`字段，并调用`notifyDataSetChanged()`刷新整个列表。这样做呢实现起来比较简单,但是如果列表条目过多的话会稍微卡顿下。本片文章会采用另外一种方式实现,相对来说比较优雅吧
# 实现方案
首先我们看下效果图

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191111211022361.gif)

这里我们主要使用`RecyclerView`的` findViewHolderForLayoutPosition()`方法，这个方法会获取某个`postion`的`ViewHolder`，但是请注意,它的返回值可能为空,用的时候需要特别小心,空就表示屏幕看不见了

我们首先定义数据源`ItemBean`
```java
**
 * @author HuangFusheng
 * @date 2019-11-11
 * description 列表的Item数据
 */
public class ItemBean {
    private String name;
    private boolean isSelected;

    public ItemBean(String name) {
        this.name = name;
    }

    public ItemBean(String name, boolean isSelected) {
        this.name = name;
        setSelected(isSelected);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```
里面同样有个`isSelected`,但是我们不会像前言里面那样去使用这个字段,我们只是用它来表示是否选中
接下来我们就要编写我们最主要的`Adapter`了,基本的定义实现我们这里就不详细讲解了,我们只说比较重要的地方,其他的看下代码就行

第一步:在我们的`Adapter`中定义一个变量`mSelectedPos`,它表示我们当前选中的位置,当我们点击其他`Item`的时候,我们就把这个`mSelectedPos`改为那条`Item`的`position`,说到这里估计明白我们要实现的原理了,我们的核心思想就是定向刷新两个变动的`Item`，一个是原来选中的,另一个就是我们点击的,这样的话只改动了变化的部分，不会重走`onBindViewHolder()`方法，属于手动部分绑定。

第二步:我们在我们`Adapter`的构造方法中通过`isSelected`字段获取初始化的`mSelectedPos`
```java
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
```

第三步:在我们点击`Item`的时候,通过`findViewHolderForLayoutPosition()`这个方法拿到`mSelectedPos`对应的`ViewHolder`,把这条`Item`里面的控件置位未选中,然后把点击的`position`的值赋给`mSelectedPos`,然后再把`position`位置的控件置位选中状态,操作的同时我们也要改变数据源的状态

```java
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
```
Ok,到此我们就大功告成了,贴下完整的`Adapter`代码
```java
/**
 * @author HuangFusheng
 * @date 2019-11-11
 * description 适配器
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private static final String TAG = "ItemAdapter";

    private List<ItemBean> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    /**
     * 当前选中的位置
     */
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
        Log.e(TAG, "执行onBindViewHolder: .....");
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
```

这里我们操作下,切换几个`Item`,看看是否会执行多次`onBindViewHolder()()`方法

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191111211113855.gif)
我们看到我们这种方式不会多次执行`onBindViewHolder()`,小伙伴们可以尝试下

[源码地址](https://github.com/Greathfs/RecyclerViewSingleSelect)
