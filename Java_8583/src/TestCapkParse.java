
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestCapkParse {
    public static void main(String[] args) {
        String caStr = "200101000000B00101A00000033309EB374DFC5A96B71D2863875EDA2EAFB96B1B439D3ECE0B1826A2672EEEFA7990286776F8BD989A15141A75C384DFC14FEF9243AAB32707659BE9E4797A247C2F0B6D99372F384AF62FE23BC54BCDC57A9ACD1D5585C303F201EF4E8B806AFB809DB1A3DB1CD112AC884F164A67B99C7D6E5A8A6DF1D3CAE6D7ED3D5BE725B2DE4ADE23FA679BF4EB15A93D8A6E29C7FFA1A70DE2E54F593D908A3BF9EBBD760BBFDC8DB8B54497E6C5BE0E4A4DAC29E503A075306EAB0045BAF72CDD33B3B678779DE1F527A72B736C078E4A4C4B346928CB6C4273B7A6AC1407B01E4F79B606CE32974ABFD78D140CE5BE3A3A8EBDF7D987323174B9072EF0AF61CBB1EC30AFE827E64FF01D37002B6DC3E23FDAF15E8C4A08E1A5BED5D57FAC8E873C054E6434033FD9F0ACD9B3CF87C18DF1E66B66AE1BCFBC55752D5A3EC87CB077BD7AAB89015740DBE5172F8222CC5A7FDC51145F80408AFA8B558BE0729EB54302FA50051748233AB14496808B3113D153BA598E8645B394";
        String data = changeCASMHashData(caStr);
        System.out.println(data);
        new TLVModelList(data);
    }

    public static String changeCASMHashData(String caStr) {
        int offset = 0;
        int caModeLenth = 0;
        //公钥指数长度
        int cazsLenth = 0;
        //认证中心算法标识
        String strDF07 = null;
        //RID
        String str9F06 = null;
        //公钥索引
        String str9F22 = null;
        //认证中心公钥模
        String strDF02 = null;
        //认证中心公钥指数
        String strDF04 = null;
        //认证中心哈希校验值
        String strDF03 = null;
        if (caStr != null) {
            byte[] caData = ByteUtils.hexString2Bytes(caStr);
            offset += 5;
            byte[] caModeLenthByte = new byte[2];
            System.arraycopy(caData, offset, caModeLenthByte, 0, caModeLenthByte.length);//从报文中截取长度信息的值
            caModeLenth = Integer.parseInt(ByteUtils.bytes2HexString(caModeLenthByte), 16);//将长度内容转化为整型
            offset += 2;
            byte[] strDF07Byte = new byte[1];//构造一个字节数组用于存放长度类型的值
            System.arraycopy(caData, offset, strDF07Byte, 0, strDF07Byte.length);
            strDF07 = ByteUtils.bytes2HexString(strDF07Byte);
            offset += 1;
            byte[] cazsLenthByte = new byte[1]; //构造一个字节数组用于存放长度类型的值
            System.arraycopy(caData, offset, cazsLenthByte, 0, cazsLenthByte.length);        //从报文中截取长度信息的值
            cazsLenth = Integer.parseInt(ByteUtils.bytes2HexString(cazsLenthByte), 16);    //将长度内容转化为整型
            offset += 1;
            byte[] str9F06Byte = new byte[5];
            System.arraycopy(caData, offset, str9F06Byte, 0, str9F06Byte.length);
            str9F06 = ByteUtils.bytes2HexString(str9F06Byte);
            offset += 5;
            byte[] str9F22Byte = new byte[1];
            System.arraycopy(caData, offset, str9F22Byte, 0, str9F22Byte.length);
            str9F22 = ByteUtils.bytes2HexString(str9F22Byte);
            offset += 1;
            byte[] strDF02Byte = new byte[caModeLenth];
            System.arraycopy(caData, offset, strDF02Byte, 0, strDF02Byte.length);
            strDF02 = ByteUtils.bytes2HexString(strDF02Byte);
            offset += caModeLenth;
            byte[] strDF04Byte = new byte[cazsLenth];
            System.arraycopy(caData, offset, strDF04Byte, 0, strDF04Byte.length);
            strDF04 = ByteUtils.bytes2HexString(strDF04Byte);
            offset += cazsLenth;
            byte[] strDF03Byte = new byte[20];
            System.arraycopy(caData, offset, strDF03Byte, 0, strDF03Byte.length);
            strDF03 = ByteUtils.bytes2HexString(strDF03Byte);
            offset += 20;
            Map<String, String> caMap = new HashMap<>();
            caMap.put("9F06", str9F06);
            caMap.put("9F22", str9F22);
            caMap.put("DF05", "31270121");
            caMap.put("DF06", "01");
            caMap.put("DF07", strDF07);
            caMap.put("DF02", strDF02);
            caMap.put("DF04", strDF04);
            caMap.put("DF03", strDF03);

            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, String>> entries = caMap.entrySet();
            for (Map.Entry<String, String> en : entries) {
                System.out.println(en.getKey() + ">>" + en.getValue());
                builder.append(getTlvFormatData(en.getKey(), en.getValue()));
            }
            return builder.toString();
        }
        return "";

    }

    public static String getTlvFormatData(String tag, String value) {
        String lenTag = "";
        String hexLength = Integer.toHexString(value.length() / 2).toUpperCase();
        if (hexLength.length() % 2 != 0) {
            hexLength = "0" + hexLength;
        }
        if (value.length() / 2 >= 256) {
            lenTag = "82";
            lenTag += hexLength;
        } else if (value.length() / 2 >= 128) {
            lenTag = "81";
            lenTag += hexLength;
        } else {
            lenTag += hexLength;
        }
        return tag + lenTag + value;
    }
}
