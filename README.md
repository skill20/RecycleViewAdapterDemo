# RecycleViewAdapterDemo
common adapter

## RecycleView.Adapter的封装
##### Android RecycleView.Apdater 封装，包括开始请求网络数据的加载、加载失败、加载数据为空，支持多种Item类型。
### 使用
- 默认开始加载转圈，通过下面方法可以自定义
```java
public void setRequestLoad(boolean load) {
        this.requestLoad = load;
    }
```
- 单一Item数据绑定
```java
adapterWrapper = new AdapterWrapper<String>(this, R.layout.recyele_list_def) {

            @Override
            protected void convert(ViewHolder holder, String item) {
                holder.setText(R.id.text, item);
            }
        };
```
- 多种ItemType
```java
 wrapper = new AdapterWrapper<MulBean>(this, new MutSupport()) {

            @Override
            protected void convert(ViewHolder holder, MulBean item) {
                if (item.type == 2) {
                    holder.setText(R.id.text, item.str);
                } else if (item.type == 3) {
                    holder.setText(R.id.text_mul, item.str);
                }
            }
        };

	static class MutSupport implements AdapterWrapper.MultiItemTypeSupport<MulBean> {

        @Override
        public int getLayoutId(int itemType) {
            if (itemType == 2) {
                return R.layout.recyele_list_def;
            } else if (itemType == 3) {
                return R.layout.recyele_list_mul;
            }
            return 0;
        }

        @Override
        public int getItemViewType(int position, MulBean mulBean) {
            return mulBean.type;
        }
    }

```
## 感谢


https://github.com/hongyangAndroid/base-adapter

对鸿洋的adapter参考。