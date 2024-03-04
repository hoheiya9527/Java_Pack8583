
public class Iso8583Util {

    public static final String TAG = "Iso8583Util";

    /**
     * 仅报文体：消费类型+位图+各域信息
     *
     * @param stIso8583
     * @return
     * @throws Exception
     */
    public static byte[] iso8583PackageBody(StIso8583 stIso8583) throws Exception {
        Log.e(TAG, "iso8583PackageBody");
        stIso8583.toLogModel();
        int fieldCount = StIso8583Wrap.Field_Count_128;//Field_Count_64;
//        if (!TextUtils.isEmpty(stIso8583.field102)
//                || !TextUtils.isEmpty(stIso8583.field103)
//                || !TextUtils.isEmpty(stIso8583.field128)) {
//            fieldCount = StIso8583Wrap.Field_Count_128;
//        }
        byte[] dataBuff = new byte[1024];
        int index = 0;
        StringBuilder bitmapStringBuffer = new StringBuilder();
//            if (fieldCount == StIso8583Wrap.Field_Count_128) {
        bitmapStringBuffer.append("1");
//            } else {
//                bitmapStringBuffer.append("0");
//            }
        StIso8583Wrap stIso8583Wrap = new StIso8583Wrap();
        for (int fieldIndex = 2; fieldIndex <= fieldCount; fieldIndex++) {
            byte[] fieldBuff;
            byte[] lenBuff = new byte[0];
            String fieldValue = getFieldValue(stIso8583, fieldIndex);
            if (fieldValue == null || fieldValue.isEmpty()) {
                bitmapStringBuffer.append("0");
                continue;
            }
            bitmapStringBuffer.append("1");
            FieldModel dataModel = stIso8583Wrap.getFiled(fieldIndex);
            if (dataModel == null) {
                throw new Exception("Field[" + fieldIndex + "]未配置");
            }
            int dataType = dataModel.type;
            int lenLen = dataModel.lenLen;
            int lenType = dataModel.lenType;
            if (dataType == StIso8583Wrap.Type_A) {
                fieldBuff = fieldValue.getBytes("GBK");
            } else {
                if (fieldValue.length() % 2 != 0) {
                    //TYPE_N
//                    if (dataType == StIso8583Wrap.Type_N && lenLen == StIso8583Wrap.LenLen_fix) {
                    fieldBuff = ByteUtils.hexString2Bytes("0" + fieldValue);
//                    } else {
//                        fieldBuff = ByteUtils.hexString2Bytes(fieldValue + "0");
//                    }
                } else {
                    fieldBuff = ByteUtils.hexString2Bytes(fieldValue);
                }
            }
            if (lenLen != StIso8583Wrap.LenLen_fix) {
                //长度以16进制表示
                String lenValue;
                if (dataType == StIso8583Wrap.Type_A || dataType == StIso8583Wrap.Type_B) {
                    String lengthHex = Integer.toHexString(fieldBuff.length);
                    if (lenType == StIso8583Wrap.LenType_A) {
                        lenValue = ByteUtils.addZero(lengthHex, lenLen, true);
                    } else {
                        lenValue = ByteUtils.addZero(lengthHex, lenLen * 2, true);
                    }
                } else {
                    String lengthHex = Integer.toHexString(fieldValue.length());
                    lenValue = ByteUtils.addZero(lengthHex, lenLen * 2, true);
                }
                if (lenType == StIso8583Wrap.LenType_A) {
                    lenBuff = lenValue.getBytes();
                } else {
                    lenBuff = ByteUtils.hexString2Bytes(lenValue);
                }
            }
            System.arraycopy(lenBuff, 0, dataBuff, index, lenBuff.length);
            index += lenBuff.length;
            System.arraycopy(fieldBuff, 0, dataBuff, index, fieldBuff.length);
            index += fieldBuff.length;
        }
//             Log.e(TAG,("SendBuff:" + ByteUtils.bytes2HexString(dataBuff));

        String bitmapStr = bitmapStringBuffer.toString();
//            if (fieldCount == StIso8583Wrap.Field_Count_128) {
        bitmapStr = "1" + bitmapStr.substring(1);
//            } else {
//                bitmapStr = "0" + bitmapStr.substring(1);
//            }
//            Log.e(TAG, "Bitmap:" + bitmapStr);
        byte[] bitmapBuff = ByteUtils.convertBinaryToByte(bitmapStr);
        Log.e(TAG, "Bitmap:" + ByteUtils.bytes2HexString(bitmapBuff));
        bitmapStringBuffer.setLength(0);
        byte[] msgTypeBuff = ByteUtils.hexString2Bytes(ByteUtils.bytes2HexString(stIso8583.szMsgCode.getBytes()));
        int packageLen = msgTypeBuff.length + bitmapBuff.length + index;
        byte[] totalDataBuff = new byte[packageLen];
        index = 0;
        System.arraycopy(msgTypeBuff, 0, totalDataBuff, index, msgTypeBuff.length);
        index += msgTypeBuff.length;
        System.arraycopy(bitmapBuff, 0, totalDataBuff, index, bitmapBuff.length);
        index += bitmapBuff.length;
        System.arraycopy(dataBuff, 0, totalDataBuff, index, totalDataBuff.length - index);
//            Log.e(TAG, "TotalSendBuff:" + ByteUtils.bytes2HexString(totalDataBuff));
        return totalDataBuff;
    }

    /**
     * TPDU+报文头+报文体
     *
     * @param stIso8583
     * @return
     * @throws Exception
     */
    public static byte[] iso8583Package(StIso8583 stIso8583, String tpdu, String header) throws Exception {
        Log.e(TAG, "iso8583Package");
        try {
            //
            //报文头结构 ：报文长度[2] + 协议[1] + 会晤[1] + 私域终端号[4]
            byte[] chat = "1".getBytes();//MerchantParams.isGuoMi() ? "G".getBytes() : "1".getBytes();
            String headerNoLen = header.substring(4);
            //
            byte[] body = iso8583PackageBody(stIso8583);
            byte[] tpduBuff = ByteUtils.hexString2Bytes(tpdu);
            //计算报文头中的报文长度=报文头自身长度+报文体长度
            int lenInHeader = 2 + headerNoLen.length() / 2 + body.length;
            String lenInHeaderStr = ByteUtils.addZero(Integer.toHexString(lenInHeader), 4, true);
            byte[] headerBuff = ByteUtils.hexString2Bytes(lenInHeaderStr + headerNoLen);
            int packageLen = tpduBuff.length + lenInHeader;
            byte[] totalDataBuff = new byte[packageLen];
            int index = 0;
//            lenBuff[0] = (byte) (packageLen >> 8);
//            lenBuff[1] = (byte) packageLen;
//            System.arraycopy(lenBuff, 0, totalDataBuff, index, lenBuff.length);
//            index += lenBuff.length;
            System.arraycopy(tpduBuff, 0, totalDataBuff, index, tpduBuff.length);
            index += tpduBuff.length;
            System.arraycopy(headerBuff, 0, totalDataBuff, index, headerBuff.length);
            index += headerBuff.length;
            System.arraycopy(body, 0, totalDataBuff, index, body.length);
//            Log.e(TAG, "TotalSendBuff:" + ByteUtils.bytes2HexString(totalDataBuff));
            return totalDataBuff;
        } catch (Exception e) {
            throw new Exception("8583组包错误，" + e.getMessage());
        }
    }

    public static StIso8583 iso8583UnPackageBody(byte[] dataBuff) throws Exception {
        StIso8583 stIso8583 = new StIso8583();
        Log.e(TAG, "iso8583UnPackageBody: dataBuff:" + ByteUtils.bytes2HexString(dataBuff));
        int index = 0;
        //消息类型[4]+位图[16]+各域
        byte[] msgTypeBuff = new byte[4];
        byte[] bitmapBuff = new byte[16];
//            byte[] bitmapDefultBuff = new byte[8];
        byte[] bitmapLeftBuff = new byte[8];
        byte[] bitmapRightBuff = new byte[8];
        int fieldCount = StIso8583Wrap.Field_Count_128;//StIso8583Wrap.Field_Count_64;
        System.arraycopy(dataBuff, index, msgTypeBuff, 0, msgTypeBuff.length);
        index += msgTypeBuff.length;
//            System.arraycopy(dataBuff, index, bitmapDefultBuff, 0, bitmapDefultBuff.length);
//            String bitmapDefaultStr = ByteUtils.bytes2HexString(bitmapDefultBuff);
//            Log.e(TAG, "BitmapDefault:" + bitmapDefaultStr);
//            String bitmapDefaultBinary = ByteUtils.convertByteToBinary(bitmapDefultBuff);
//            Log.e(TAG, "bitmapDefaultBinary:" + bitmapDefaultBinary);
//            if (bitmapDefaultBinary.startsWith("1")) {
//                bitmapBuff = new byte[16];
//                fieldCount = StIso8583Wrap.Field_Count_128;
//            }
        stIso8583.szMsgCode = new String(ByteUtils.hexString2Bytes(ByteUtils.bytes2HexString(msgTypeBuff)));
        Log.e(TAG, "MsgCode:" + stIso8583.szMsgCode);
        System.arraycopy(dataBuff, index, bitmapBuff, 0, bitmapBuff.length);
        index += bitmapBuff.length;
        String bitmapStr = ByteUtils.bytes2HexString(bitmapBuff);
        Log.e(TAG, "Bitmap:" + bitmapStr);
        String bitmapBinary = "";
//            if (fieldCount == StIso8583Wrap.Field_Count_128) {
        System.arraycopy(bitmapBuff, 0, bitmapLeftBuff, 0, 8);
        System.arraycopy(bitmapBuff, 8, bitmapRightBuff, 0, 8);
        bitmapBinary = ByteUtils.convertByteToBinary(bitmapLeftBuff) + ByteUtils.convertByteToBinary(bitmapRightBuff);
//            } else {
//                bitmapBinary = ByteUtils.convertByteToBinary(bitmapBuff);
//            }
//            Log.e(TAG, "bitmapBinary:" + bitmapBinary);
        StIso8583Wrap stIso8583Wrap = new StIso8583Wrap();
        for (int fieldIndex = 2; fieldIndex <= fieldCount; fieldIndex++) {
            byte[] lenBuff;
            byte[] fieldBuff;
            String fieldValue = "";
            if (!"1".equals(bitmapBinary.substring(fieldIndex - 1, fieldIndex))) {
                continue;
            }
            FieldModel dataModel = stIso8583Wrap.getFiled(fieldIndex);
            if (dataModel == null) {
                throw new Exception("Field[" + fieldIndex + "]未配置");
            }
            int dataType = dataModel.type;
            int lenLen = dataModel.lenLen;
            int lenType = dataModel.lenType;
            int dataLen = dataModel.len;
            if (lenLen != StIso8583Wrap.LenLen_fix) {
                lenBuff = new byte[lenLen];
                System.arraycopy(dataBuff, index, lenBuff, 0, lenBuff.length);
                if (lenType == StIso8583Wrap.LenType_A) {
                    dataLen = Integer.parseInt(new String(lenBuff), 16);
                } else {
                    dataLen = Integer.parseInt(ByteUtils.bytes2HexString(lenBuff), 16);
                }
                index += lenBuff.length;
            }
            if (dataType == StIso8583Wrap.Type_A || dataType == StIso8583Wrap.Type_B) {
                fieldBuff = new byte[dataLen];
                System.arraycopy(dataBuff, index, fieldBuff, 0, fieldBuff.length);
                index += fieldBuff.length;
                fieldValue = (dataType == StIso8583Wrap.Type_A ? new String(fieldBuff, "GBK")
                        : ByteUtils.bytes2HexString(fieldBuff));
            } else {
                //TYPE_N
                fieldBuff = new byte[(dataLen + 1) / 2];
                System.arraycopy(dataBuff, index, fieldBuff, 0, fieldBuff.length);
                index += fieldBuff.length;
                String hexString = ByteUtils.bytes2HexString(fieldBuff);
//                if (lenLen == StIso8583Wrap.LenLen_fix) {
                fieldValue = hexString.substring(hexString.length() - dataLen);
//                } else {
//                    fieldValue = hexString.substring(0, dataLen);
//                }
            }
            Log.e(TAG, "Set Field[ " + fieldIndex + " ]\tLen[" + dataLen + "]：\t" + fieldValue);
            setFieldValue(stIso8583, fieldIndex, fieldValue);
        }
        return stIso8583;
    }

    public static StIso8583 iso8583UnPackage(byte[] dataBuff) throws Exception {
        try {
            Log.e(TAG, "iso8583UnPackage: dataBuff:" + ByteUtils.bytes2HexString(dataBuff));
            int index = 0;
            //TPDU[5]+报文头[8]+报文体(消息类型[4]+位图[16]+各域)
            byte[] tpduBuff = new byte[5];
            byte[] headerBuff = new byte[8];
            System.arraycopy(dataBuff, index, tpduBuff, 0, tpduBuff.length);
            index += tpduBuff.length;
            System.arraycopy(dataBuff, index, headerBuff, 0, headerBuff.length);
            index += headerBuff.length;
            Log.e(TAG, "TPDU:" + ByteUtils.bytes2HexString(tpduBuff));
            String header = ByteUtils.bytes2HexString(headerBuff);
            Log.e(TAG, "Header:" + header);
            byte[] bodyBuff = new byte[dataBuff.length - index];
            System.arraycopy(dataBuff, index, bodyBuff, 0, bodyBuff.length);
            StIso8583 stIso8583 = iso8583UnPackageBody(bodyBuff);
            stIso8583.header = header;
            return stIso8583;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("8583解包错误，" + e.getMessage());
        }
    }

    private static void setFieldValue(StIso8583 stIso8583, int fieldIndex, String fieldValue) {
        switch (fieldIndex) {
            case 1:
                stIso8583.field1 = fieldValue;
                break;
            case 2:
                stIso8583.field2 = fieldValue;
                break;
            case 3:
                stIso8583.field3 = fieldValue;
                break;
            case 4:
                stIso8583.field4 = fieldValue;
                break;
            case 5:
                stIso8583.field5 = fieldValue;
                break;
            case 6:
                stIso8583.field6 = fieldValue;
                break;
            case 7:
                stIso8583.field7 = fieldValue;
                break;
            case 8:
                stIso8583.field8 = fieldValue;
                break;
            case 9:
                stIso8583.field9 = fieldValue;
                break;
            case 10:
                stIso8583.field10 = fieldValue;
                break;
            case 11:
                stIso8583.field11 = fieldValue;
                break;
            case 12:
                stIso8583.field12 = fieldValue;
                break;
            case 13:
                stIso8583.field13 = fieldValue;
                break;
            case 14:
                stIso8583.field14 = fieldValue;
                break;
            case 15:
                stIso8583.field15 = fieldValue;
                break;
            case 16:
                stIso8583.field16 = fieldValue;
                break;
            case 17:
                stIso8583.field17 = fieldValue;
                break;
            case 18:
                stIso8583.field18 = fieldValue;
                break;
            case 19:
                stIso8583.field19 = fieldValue;
                break;
            case 20:
                stIso8583.field20 = fieldValue;
                break;
            case 21:
                stIso8583.field21 = fieldValue;
                break;
            case 22:
                stIso8583.field22 = fieldValue;
                break;
            case 23:
                stIso8583.field23 = fieldValue;
                break;
            case 24:
                stIso8583.field24 = fieldValue;
                break;
            case 25:
                stIso8583.field25 = fieldValue;
                break;
            case 26:
                stIso8583.field26 = fieldValue;
                break;
            case 27:
                stIso8583.field27 = fieldValue;
                break;
            case 28:
                stIso8583.field28 = fieldValue;
                break;
            case 29:
                stIso8583.field29 = fieldValue;
                break;
            case 30:
                stIso8583.field30 = fieldValue;
                break;
            case 31:
                stIso8583.field31 = fieldValue;
                break;
            case 32:
                stIso8583.field32 = fieldValue;
                break;
            case 33:
                stIso8583.field33 = fieldValue;
                break;
            case 34:
                stIso8583.field34 = fieldValue;
                break;
            case 35:
                stIso8583.field35 = fieldValue;
                break;
            case 36:
                stIso8583.field36 = fieldValue;
                break;
            case 37:
                stIso8583.field37 = fieldValue;
                break;
            case 38:
                stIso8583.field38 = fieldValue;
                break;
            case 39:
                stIso8583.field39 = fieldValue;
                break;
            case 40:
                stIso8583.field40 = fieldValue;
                break;
            case 41:
                stIso8583.field41 = fieldValue;
                break;
            case 42:
                stIso8583.field42 = fieldValue;
                break;
            case 43:
                stIso8583.field43 = fieldValue;
                break;
            case 44:
                stIso8583.field44 = fieldValue;
                break;
            case 45:
                stIso8583.field45 = fieldValue;
                break;
            case 46:
                stIso8583.field46 = fieldValue;
                break;
            case 47:
                stIso8583.field47 = fieldValue;
                break;
            case 48:
                stIso8583.field48 = fieldValue;
                break;
            case 49:
                stIso8583.field49 = fieldValue;
                break;
            case 50:
                stIso8583.field50 = fieldValue;
                break;
            case 51:
                stIso8583.field51 = fieldValue;
                break;
            case 52:
                stIso8583.field52 = fieldValue;
                break;
            case 53:
                stIso8583.field53 = fieldValue;
                break;
            case 54:
                stIso8583.field54 = fieldValue;
                break;
            case 55:
                stIso8583.field55 = fieldValue;
                break;
            case 56:
                stIso8583.field56 = fieldValue;
                break;
            case 57:
                stIso8583.field57 = fieldValue;
                break;
            case 58:
                stIso8583.field58 = fieldValue;
                break;
            case 59:
                stIso8583.field59 = fieldValue;
                break;
            case 60:
                stIso8583.field60 = fieldValue;
                break;
            case 61:
                stIso8583.field61 = fieldValue;
                break;
            case 62:
                stIso8583.field62 = fieldValue;
                break;
            case 63:
                stIso8583.field63 = fieldValue;
                break;
            case 64:
                stIso8583.field64 = fieldValue;
                break;
            case 65:
                stIso8583.field65 = fieldValue;
                break;
            case 66:
                stIso8583.field66 = fieldValue;
                break;
            case 67:
                stIso8583.field67 = fieldValue;
                break;
            case 68:
                stIso8583.field68 = fieldValue;
                break;
            case 69:
                stIso8583.field69 = fieldValue;
                break;
            case 70:
                stIso8583.field70 = fieldValue;
                break;
            case 71:
                stIso8583.field71 = fieldValue;
                break;
            case 72:
                stIso8583.field72 = fieldValue;
                break;
            case 73:
                stIso8583.field73 = fieldValue;
                break;
            case 74:
                stIso8583.field74 = fieldValue;
                break;
            case 75:
                stIso8583.field75 = fieldValue;
                break;
            case 76:
                stIso8583.field76 = fieldValue;
                break;
            case 77:
                stIso8583.field77 = fieldValue;
                break;
            case 78:
                stIso8583.field78 = fieldValue;
                break;
            case 79:
                stIso8583.field79 = fieldValue;
                break;
            case 80:
                stIso8583.field80 = fieldValue;
                break;
            case 81:
                stIso8583.field81 = fieldValue;
                break;
            case 82:
                stIso8583.field82 = fieldValue;
                break;
            case 83:
                stIso8583.field83 = fieldValue;
                break;
            case 84:
                stIso8583.field84 = fieldValue;
                break;
            case 85:
                stIso8583.field85 = fieldValue;
                break;
            case 86:
                stIso8583.field86 = fieldValue;
                break;
            case 87:
                stIso8583.field87 = fieldValue;
                break;
            case 88:
                stIso8583.field88 = fieldValue;
                break;
            case 89:
                stIso8583.field89 = fieldValue;
                break;
            case 90:
                stIso8583.field90 = fieldValue;
                break;
            case 91:
                stIso8583.field91 = fieldValue;
                break;
            case 92:
                stIso8583.field92 = fieldValue;
                break;
            case 93:
                stIso8583.field93 = fieldValue;
                break;
            case 94:
                stIso8583.field94 = fieldValue;
                break;
            case 95:
                stIso8583.field95 = fieldValue;
                break;
            case 96:
                stIso8583.field96 = fieldValue;
                break;
            case 97:
                stIso8583.field97 = fieldValue;
                break;
            case 98:
                stIso8583.field98 = fieldValue;
                break;
            case 99:
                stIso8583.field99 = fieldValue;
                break;
            case 100:
                stIso8583.field100 = fieldValue;
                break;
            case 101:
                stIso8583.field101 = fieldValue;
                break;
            case 102:
                stIso8583.field102 = fieldValue;
                break;
            case 103:
                stIso8583.field103 = fieldValue;
                break;
            case 104:
                stIso8583.field104 = fieldValue;
                break;
            case 105:
                stIso8583.field105 = fieldValue;
                break;
            case 106:
                stIso8583.field106 = fieldValue;
                break;
            case 107:
                stIso8583.field107 = fieldValue;
                break;
            case 108:
                stIso8583.field108 = fieldValue;
                break;
            case 109:
                stIso8583.field109 = fieldValue;
                break;
            case 110:
                stIso8583.field110 = fieldValue;
                break;
            case 111:
                stIso8583.field111 = fieldValue;
                break;
            case 112:
                stIso8583.field112 = fieldValue;
                break;
            case 113:
                stIso8583.field113 = fieldValue;
                break;
            case 114:
                stIso8583.field114 = fieldValue;
                break;
            case 115:
                stIso8583.field115 = fieldValue;
                break;
            case 116:
                stIso8583.field116 = fieldValue;
                break;
            case 117:
                stIso8583.field117 = fieldValue;
                break;
            case 118:
                stIso8583.field118 = fieldValue;
                break;
            case 119:
                stIso8583.field119 = fieldValue;
                break;
            case 120:
                stIso8583.field120 = fieldValue;
                break;
            case 121:
                stIso8583.field121 = fieldValue;
                break;
            case 122:
                stIso8583.field122 = fieldValue;
                break;
            case 123:
                stIso8583.field123 = fieldValue;
                break;
            case 124:
                stIso8583.field124 = fieldValue;
                break;
            case 125:
                stIso8583.field125 = fieldValue;
                break;
            case 126:
                stIso8583.field126 = fieldValue;
                break;
            case 127:
                stIso8583.field127 = fieldValue;
                break;
            case 128:
                stIso8583.field128 = fieldValue;
            default:
                break;
        }
    }

    private static String getFieldValue(StIso8583 stIso8583, int fieldIndex) {
        switch (fieldIndex) {
            case 1:
                return stIso8583.field1;
            case 2:
                return stIso8583.field2;
            case 3:
                return stIso8583.field3;
            case 4:
                return stIso8583.field4;
            case 5:
                return stIso8583.field5;
            case 6:
                return stIso8583.field6;
            case 7:
                return stIso8583.field7;
            case 8:
                return stIso8583.field8;
            case 9:
                return stIso8583.field9;
            case 10:
                return stIso8583.field10;
            case 11:
                return stIso8583.field11;
            case 12:
                return stIso8583.field12;
            case 13:
                return stIso8583.field13;
            case 14:
                return stIso8583.field14;
            case 15:
                return stIso8583.field15;
            case 16:
                return stIso8583.field16;
            case 17:
                return stIso8583.field17;
            case 18:
                return stIso8583.field18;
            case 19:
                return stIso8583.field19;
            case 20:
                return stIso8583.field20;
            case 21:
                return stIso8583.field21;
            case 22:
                return stIso8583.field22;
            case 23:
                return stIso8583.field23;
            case 24:
                return stIso8583.field24;
            case 25:
                return stIso8583.field25;
            case 26:
                return stIso8583.field26;
            case 27:
                return stIso8583.field27;
            case 28:
                return stIso8583.field28;
            case 29:
                return stIso8583.field29;
            case 30:
                return stIso8583.field30;
            case 31:
                return stIso8583.field31;
            case 32:
                return stIso8583.field32;
            case 33:
                return stIso8583.field33;
            case 34:
                return stIso8583.field34;
            case 35:
                return stIso8583.field35;
            case 36:
                return stIso8583.field36;
            case 37:
                return stIso8583.field37;
            case 38:
                return stIso8583.field38;
            case 39:
                return stIso8583.field39;
            case 40:
                return stIso8583.field40;
            case 41:
                return stIso8583.field41;
            case 42:
                return stIso8583.field42;
            case 43:
                return stIso8583.field43;
            case 44:
                return stIso8583.field44;
            case 45:
                return stIso8583.field45;
            case 46:
                return stIso8583.field46;
            case 47:
                return stIso8583.field47;
            case 48:
                return stIso8583.field48;
            case 49:
                return stIso8583.field49;
            case 50:
                return stIso8583.field50;
            case 51:
                return stIso8583.field51;
            case 52:
                return stIso8583.field52;
            case 53:
                return stIso8583.field53;
            case 54:
                return stIso8583.field54;
            case 55:
                return stIso8583.field55;
            case 56:
                return stIso8583.field56;
            case 57:
                return stIso8583.field57;
            case 58:
                return stIso8583.field58;
            case 59:
                return stIso8583.field59;
            case 60:
                return stIso8583.field60;
            case 61:
                return stIso8583.field61;
            case 62:
                return stIso8583.field62;
            case 63:
                return stIso8583.field63;
            case 64:
                return stIso8583.field64;
            case 65:
                return stIso8583.field65;
            case 66:
                return stIso8583.field66;
            case 67:
                return stIso8583.field67;
            case 68:
                return stIso8583.field68;
            case 69:
                return stIso8583.field69;
            case 70:
                return stIso8583.field70;
            case 71:
                return stIso8583.field71;
            case 72:
                return stIso8583.field72;
            case 73:
                return stIso8583.field73;
            case 74:
                return stIso8583.field74;
            case 75:
                return stIso8583.field75;
            case 76:
                return stIso8583.field76;
            case 77:
                return stIso8583.field77;
            case 78:
                return stIso8583.field78;
            case 79:
                return stIso8583.field79;
            case 80:
                return stIso8583.field80;
            case 81:
                return stIso8583.field81;
            case 82:
                return stIso8583.field82;
            case 83:
                return stIso8583.field83;
            case 84:
                return stIso8583.field84;
            case 85:
                return stIso8583.field85;
            case 86:
                return stIso8583.field86;
            case 87:
                return stIso8583.field87;
            case 88:
                return stIso8583.field88;
            case 89:
                return stIso8583.field89;
            case 90:
                return stIso8583.field90;
            case 91:
                return stIso8583.field91;
            case 92:
                return stIso8583.field92;
            case 93:
                return stIso8583.field93;
            case 94:
                return stIso8583.field94;
            case 95:
                return stIso8583.field95;
            case 96:
                return stIso8583.field96;
            case 97:
                return stIso8583.field97;
            case 98:
                return stIso8583.field98;
            case 99:
                return stIso8583.field99;
            case 100:
                return stIso8583.field100;
            case 101:
                return stIso8583.field101;
            case 102:
                return stIso8583.field102;
            case 103:
                return stIso8583.field103;
            case 104:
                return stIso8583.field104;
            case 105:
                return stIso8583.field105;
            case 106:
                return stIso8583.field106;
            case 107:
                return stIso8583.field107;
            case 108:
                return stIso8583.field108;
            case 109:
                return stIso8583.field109;
            case 110:
                return stIso8583.field110;
            case 111:
                return stIso8583.field111;
            case 112:
                return stIso8583.field112;
            case 113:
                return stIso8583.field113;
            case 114:
                return stIso8583.field114;
            case 115:
                return stIso8583.field115;
            case 116:
                return stIso8583.field116;
            case 117:
                return stIso8583.field117;
            case 118:
                return stIso8583.field118;
            case 119:
                return stIso8583.field119;
            case 120:
                return stIso8583.field120;
            case 121:
                return stIso8583.field121;
            case 122:
                return stIso8583.field122;
            case 123:
                return stIso8583.field123;
            case 124:
                return stIso8583.field124;
            case 125:
                return stIso8583.field125;
            case 126:
                return stIso8583.field126;
            case 127:
                return stIso8583.field127;
            case 128:
                return stIso8583.field128;

        }
        return "";
    }


}
