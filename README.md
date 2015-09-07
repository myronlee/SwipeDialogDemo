#Android Swipe Dialog

This is a custom dialog that can be dismissed by swiping.The features of system dialog are kept, eg cancel on back pressed, set variety of listeners and so on.

##Demo

![](http://myron-bloggif.stor.sinaapp.com/swipe_dialog.gif)

##Uasage

### Step 1

Gradle

```groovy
    compile 'com.sinaapp.myron:library:unspecified'
```

### Step 2
Just extend `SwipeDialog`, your custom dialog can be dismissed by swiping.Below is an example.
```java
public class ListDialog extends SwipeDialog {
    public ListDialog(Context context) {
        super(context);
    }

    public ListDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_listview);
        ListView listView = (ListView)findViewById(R.id.list);
        BaseAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n"});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="300dp"
    android:layout_height="400dp"
    android:background="@android:color/white">

    <ListView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_above="@+id/button2" />

    <Button
        android:id="@+id/button2"
        android:layout_width="150dp"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true"
        android:text="New Button" />

    <Button
        android:id="@+id/button3"
        android:layout_width="150dp"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_alignParentEnd="true"
        android:layout_alignParentRight="true"
        android:text="New Button" />


</RelativeLayout>
```



# How it work
[blog(2)](http://blog.csdn.net/fly1183989782/article/details/48085975)
[blog(1)](http://blog.csdn.net/fly1183989782/article/details/47208549)

