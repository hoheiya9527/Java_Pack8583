import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class StIso8583 implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    public String header = "";
    public String szMsgCode = "";
    public String field1 = "";
    public String field2 = "";
    public String field3 = "";
    public String field4 = "";
    public String field5 = "";
    public String field6 = "";
    public String field7 = "";
    public String field8 = "";
    public String field9 = "";
    public String field10 = "";
    public String field11 = "";
    public String field12 = "";
    public String field13 = "";
    public String field14 = "";
    public String field15 = "";
    public String field16 = "";
    public String field17 = "";
    public String field18 = "";
    public String field19 = "";
    public String field20 = "";
    public String field21 = "";
    public String field22 = "";
    public String field23 = "";
    public String field24 = "";
    public String field25 = "";
    public String field26 = "";
    public String field27 = "";
    public String field28 = "";
    public String field29 = "";
    public String field30 = "";
    public String field31 = "";
    public String field32 = "";
    public String field33 = "";
    public String field34 = "";
    public String field35 = "";
    public String field36 = "";
    public String field37 = "";
    public String field38 = "";
    public String field39 = "";
    public String field40 = "";
    public String field41 = "";
    public String field42 = "";
    public String field43 = "";
    public String field44 = "";
    public String field45 = "";
    public String field46 = "";
    public String field47 = "";
    public String field48 = "";
    public String field49 = "";
    public String field50 = "";
    public String field51 = "";
    public String field52 = "";
    public String field53 = "";
    public String field54 = "";
    public String field55 = "";
    public String field56 = "";
    public String field57 = "";
    public String field58 = "";
    public String field59 = "";
    public String field60 = "";
    public String field61 = "";
    public String field62 = "";
    public String field63 = "";
    public String field64 = "";
    public String field65 = "";
    public String field66 = "";
    public String field67 = "";
    public String field68 = "";
    public String field69 = "";
    public String field70 = "";
    public String field71 = "";
    public String field72 = "";
    public String field73 = "";
    public String field74 = "";
    public String field75 = "";
    public String field76 = "";
    public String field77 = "";
    public String field78 = "";
    public String field79 = "";
    public String field80 = "";
    public String field81 = "";
    public String field82 = "";
    public String field83 = "";
    public String field84 = "";
    public String field85 = "";
    public String field86 = "";
    public String field87 = "";
    public String field88 = "";
    public String field89 = "";
    public String field90 = "";
    public String field91 = "";
    public String field92 = "";
    public String field93 = "";
    public String field94 = "";
    public String field95 = "";
    public String field96 = "";
    public String field97 = "";
    public String field98 = "";
    public String field99 = "";
    public String field100 = "";
    public String field101 = "";
    public String field102 = "";
    public String field103 = "";
    public String field104 = "";
    public String field105 = "";
    public String field106 = "";
    public String field107 = "";
    public String field108 = "";
    public String field109 = "";
    public String field110 = "";
    public String field111 = "";
    public String field112 = "";
    public String field113 = "";
    public String field114 = "";
    public String field115 = "";
    public String field116 = "";
    public String field117 = "";
    public String field118 = "";
    public String field119 = "";
    public String field120 = "";
    public String field121 = "";
    public String field122 = "";
    public String field123 = "";
    public String field124 = "";
    public String field125 = "";
    public String field126 = "";
    public String field127 = "";
    public String field128 = "";


    public StIso8583 toLogModel() {
        StIso8583 stIso8583 = new StIso8583();
        try {
            stIso8583 = (StIso8583) StIso8583.this.clone();
//            stIso8583.field2 = "";
//            stIso8583.field35 = "";
//            stIso8583.field36 = "";
//            stIso8583.field52 = "";
            // 正式发布版本时给敏感信息赋值为空
            Class<? extends StIso8583> c = stIso8583.getClass();
            Log.e("posL", "-----------------------");
            for (int i = 1; i <= 128; i++) {
                // 获取该对象的propertyName成员变量
                Field field = c.getDeclaredField("field" + i);
                field.setAccessible(true);
                if (!"".equals(field.get(stIso8583))) {
                    Log.e("posL", "域" + i + "：" + field.get(stIso8583));
                }
            }
            Log.e("posL", "-----------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stIso8583;
    }

    public String getMacAll() {
        try {
            StringBuilder builder = new StringBuilder();
            StIso8583 stIso8583 = (StIso8583) StIso8583.this.clone();
            //参与MAC计算的各域
            Integer[] inMac = new Integer[]{2, 3, 4, 5, 7, 9, 10, 11, 12, 13, 14, 15, 18, 22, 23, 25,
                    28, 30, 32, 33, 35, 36, 37, 38, 39, 41, 42, 43, 44, 48, 49, 50, 53, 54,
                    55, 59, 60, 61, 62, 63, 65, 67, 70, 71, 90, 91, 95, 100, 101, 102, 103,
                    104, 105, 107, 108, 110, 116, 118, 119, 120, 122, 123};
            List<Integer> integers = Arrays.asList(inMac);
            for (int i = 1; i <= 128; i++) {
//                if (!integers.contains(i)) {
//                    // 获取该对象的propertyName成员变量
//                    Class<? extends StIso8583> c = stIso8583.getClass();
//                    Field field = c.getDeclaredField("field" + i);
//                    field.setAccessible(true);
//                    if (!"".equals(field.get(stIso8583))) {
//                        field.set(stIso8583, "");
//                        Log.e("posL", "===getMacAll===去除域:" + i);
//                    }
//                }
                Class<? extends StIso8583> c = stIso8583.getClass();
                Field field = c.getDeclaredField("field" + i);
                field.setAccessible(true);
                if (integers.contains(i)) {
                    String value = String.valueOf(field.get(stIso8583));
                    if (i == 55) {//55域保持原样
                        builder.append(value);
                        continue;
                    }
                    builder.append(ByteUtils.bytes2HexString(value.getBytes()));
                } else {
                    if (!"".equals(field.get(stIso8583))) {
                        System.out.println("=======getMacAll===去除域:" + i);
                    }
                }
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
