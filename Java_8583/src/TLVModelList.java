import com.google.common.collect.ArrayListMultimap;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TLVModelList {
    private ArrayListMultimap<String, TLVModel> modelList;

    public TLVModelList(String modelList) {
        this.modelList = this.parseTLVModelList(modelList);
    }

    public TLVModelList() {
        modelList = ArrayListMultimap.create();
    }

    public ArrayListMultimap<String, TLVModel> parseTLVModelList(String modelListString) {
        int length = 0;
        int tagLength = 2;
//        HashMap<String, TLVModel> modelList = new HashMap<String, TLVModel>();
        ArrayListMultimap<String, TLVModel> modelList = ArrayListMultimap.create();
        int asclength = 2;
        // int asclength = 6;// 长度为asc时需拼接的长度位数
        while (length < modelListString.length()) {
            TLVModel tlvModel = new TLVModel();
            // 判定tag长度，先取1字节，bit1~bit4都为1则实际Tag取2字节
            if ((Integer.parseInt(modelListString.substring(length, length + 2),
                    16) & 0x1f) == 0x1f) {
                tagLength = 4;
            } else {
                tagLength = 2;
            }
            String tag = modelListString.substring(length, length + tagLength);
            /////////////////////// length为asc的处理
            // String ascLen = modelListString.substring(length + tagLength,
            // length + tagLength + asclength);
            // int modelLen = 0;
            // try
            // {
            // modelLen = Integer.parseInt(new String(
            // ByteUtils.hexString2ByteArray(ascLen), "GBK"));
            // }
            // catch (NumberFormatException e)
            // {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // catch (UnsupportedEncodingException e)
            // {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // if (getBit(modelLen, 7))// 最高位为1，后7位表示长度字节
            // {
            // int tagLen = modelLen & 0x7f;
            // modelLen = Integer.parseInt(modelListString.substring(
            // length + tagLength + asclength,
            // (length += tagLen * 2) + tagLength + asclength));
            // }
            /////////////////////// length为bcd的处理
            int modelLen = Integer.parseInt(modelListString
                    .substring(length + tagLength, length + tagLength + 2), 16);
            if (getBit(modelLen, 7))// 最高位为1，后7位表示长度字节
            {
                int tagLen = modelLen & 0x7f;
                modelLen = Integer.parseInt(modelListString.substring(
                        length + tagLength + asclength,
                        (length += tagLen * 2) + tagLength + asclength), 16);
            }
            //
            String modelValue = modelListString.substring(
                    length + tagLength + asclength,
                    length + tagLength + asclength + modelLen * 2);
            tlvModel.setTag(tag);
            tlvModel.setLength(modelLen);
            tlvModel.setValue(modelValue);
            System.out.println(tlvModel);
            modelList.put(tag, tlvModel);
            length += tagLength + asclength + modelLen * 2;
        }
        return modelList;
    }

    public static boolean getBit(int num, int i) {
        return ((num & (1 << i)) != 0);// true 表示第i位为1,否则为0
    }

    public void addTLVModel(String tag, String value) {
        // String lenTag = Integer.toHexString(value.length());
        TLVModel tlvModel = new TLVModel();
        tlvModel.setTag(tag);
        tlvModel.setLength(value.length() / 2);
        tlvModel.setValue(value);
        modelList.put(tag, tlvModel);
    }

    public void removeTLVModel(String tag) {
        modelList.removeAll(tag);
    }

    public List<TLVModel> getTLVValue(String tag) {
        if (modelList != null)
            return modelList.get(tag);
        else
            return null;
    }


    public ArrayListMultimap<String, TLVModel> getModelList() {
        return modelList;
    }

    public void setModelList(ArrayListMultimap<String, TLVModel> modelList) {
        this.modelList = modelList;
    }


    public String getModelListString() {
        Iterator<String> iter = modelList.keySet().iterator();
        StringBuilder result = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            List<TLVModel> val = modelList.get(key);
            for (TLVModel model : val) {
                String tlvData = getTlvFormatDataHex(model.getTag(), model.getValue());
                result.append(tlvData);
            }

        }
        return result.toString();
    }

    public static String getTlvFormatDataHex(String tag, String value) {
        String lenTag = getLenHex(value.length() / 2);
        return tag + lenTag + value;
    }

    public static String getLenHex(int len) {
        String lenTag = "";
        String hexLength = Integer.toHexString(len)
                .toUpperCase(Locale.CHINA);
        if (hexLength.length() % 2 != 0) {
            hexLength = "0" + hexLength;
        }
        if (len >= 256) {
            lenTag = "82";
            lenTag += hexLength;
        } else if (len >= 128) {
            lenTag = "81";
            lenTag += hexLength;
        } else {
            lenTag += hexLength;
        }
        return lenTag;
    }


    public class TLVModel {
        private String tag;
        private int length;
        private String value;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "TLVModel [tag=" + tag + ", length=" + length + ", value="
                    + value + "]";
        }

    }
}