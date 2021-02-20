package com.kevin.hive;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.io.Text;

public class MyUDF extends GenericUDF {
    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        return null;
    }

    @Override
    public Text evaluate(DeferredObject[] arguments) throws HiveException {
        // 模拟Hive的upper方法:将第一个字符变成大写，其他的不变
        return null;
    }

    @Override
    public String getDisplayString(String[] children) {
        return null;
    }
}
