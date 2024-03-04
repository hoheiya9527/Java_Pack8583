public class Test {
    public static void main(String[] args) {
//        System.out.println("G".getBytes()[0]);//G:71---HEX---G:47,g:67

//        StIso8583 unpack = unpack();
//        String macAll = unpack.getMacAll();
//        System.out.println(macAll);

        packAndUnpack();

//        unpackBody();

//        System.out.println(isSMEncry());

//        byte[] bytes = new byte[]{0x01, (byte) 0x3D};
//        int len = (bytes[0] << 8) | bytes[1]&0xff;
//        System.out.println(len);
    }

    private static boolean isSMEncry() {
        String s = "6000000032010d67a3000000190e48697459be949e095379cc4aaecadd0ddc712688ef875d2fa37070d8abafaf4d05d3c70af24c9448d0e4fea7ddccc3d0128d85ddb561248a68aa70141d173b4dcefb8b926839dc1dc57d2db5e7999fa591b348ca80e37e3334d7d82f0521d03d815afebad66f7748c35b8d932e32e470ba05a219914992ff645e5c3cec5f8874699e49fa4ac56bb346da52f20a470e03fbe1bf81d08400ce860a3c657e988f2feb7d81cf7a4b79bb5be4ce31718e2fc77c2b66c16216a21e68dffba68880bda4785293ba602f7935503b33980bdb3a77071a988a23d17b97332f7970915ecc3807ecf7db7ec754d2b375f95a8b1a1525089bf669ffe3fdfa782cb81cc582a0a717e3adefb71d78ab2513fa043b8a7e";
        byte[] bytes = ByteUtils.hexString2Bytes(s);
        int tpduLen = 5;
        int len = (bytes[tpduLen] << 8) | bytes[tpduLen + 1];
        System.out.println("len:" + len);
        byte algorithm = bytes[tpduLen + 2];
        return algorithm == "G".getBytes()[0] || algorithm == "g".getBytes()[0];
    }

    private static void unpackBody() {
        String str = "30323030F2B80601A0C0883000000060000000011062149202116372420000000000000000030117151552190000030001151515520117007200000808752580080875258020363231343932303231313633373234323D3330313232323030303030303334383030303030303136333033323431303438313230303139000124000000000000000001310025333932302020303120202020202020202020202036302020202020202020202020202020203032303030303031303830313137313530323131303837353235383020202030383735323538302020203009D2563D3B6E0DDB";
        str += "0000000000000000";
        try {
            Iso8583Util.iso8583UnPackageBody(ByteUtils.hexString2Bytes(str));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getField60() {
        //交易代码[6]+柜员号[8]+消息类型[2]+交易类型[4]+处理能力{1]+条件代码[16]
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%-6s", "3801"));
        builder.append(String.format("%-8s", "01"));
        builder.append(String.format("%-2s", ""));
        builder.append(String.format("%-4s", ""));
        builder.append(String.format("%-1s", "6"));
        builder.append(String.format("%-16s", "0"));
        return builder.toString();
    }

    private static StIso8583 unpack() {
        StIso8583 stIso8583 = new StIso8583();
        String packStr = "01CE600032000001C931A30000001930323030F2BC0601A0C18A300000002009000001130621700720003572387600000000000000000112260942021900000300017809420212262411007200010808752580080875258025363231373030373230303033353732333837363D3234313132323032323631303230303030303030303030313933303334353630373533313030303100043030303100012400000000000000008B9F2608379430A68EA081109F2701809F101307010103A00000010A01000000000079A701E79F3704659614BB9F360205F6950500000000009A032312269C01009F02060000000000015F2A02015682027C009F1A0201569F03060000000000009F3303E0E1C89F3501229F1E0831313232333334348408A0000003330101019F090200009F41040000015300013100253338303120203031202020202020202020202020363020202020202020202020202020202030013100673131332E3335303830332B32332E3131393132315039363331303430303030333430343938303631363432323236363838202020202020202020202020202020202020202020202020202020203732333837362020202036344636413546453030303030312020D5A78F0DEF1FF3FE";
        packStr = packStr.substring(4);
//        packStr = "6000000032010b31a30000001930323130f23e060106d18611000000000000000014621700720003572387600000000000000000011222155910000090155910122224110000007200010808752580202020202020333030303030303031393330333435363037353331303030310000043030303100010000008b9f2608b2af10f245a35be69f2701809f101307010103a00000010a0100000000000e6300939f37046bc7d4799f360205b5950500000000009a032312229c01009f02060000000000015f2a02015682027c009f1a0201569f03060000000000009f3303e0e1c89f3501229f1e0831313232333334348408a0000003330101019f090200009f4104000000140000ca8b21203921b77a";
        byte[] bytes = ByteUtils.hexString2Bytes(packStr);
        try {
            stIso8583 = Iso8583Util.iso8583UnPackage(bytes);
            stIso8583.toLogModel();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stIso8583;
    }

    private static void packAndUnpack() {
        //        StIso8583 stIso8583 = getSign();
        StIso8583 stIso8583 = getSale();
        try {
            byte[] bytes = Iso8583Util.iso8583Package(stIso8583, "600028816a", "01cf6da300000058");
            System.out.println("----------");
            //TODO 计算MAC

            //叠加报文头长度
            byte[] sendData = new byte[2 + bytes.length];
            sendData[0] = (byte) (bytes.length >> 8);
            sendData[1] = (byte) bytes.length;
            System.arraycopy(bytes, 0, sendData, 2, bytes.length);
            String hexString = ByteUtils.bytes2HexString(sendData);
            System.out.println(hexString);

            /////////////////////解包
            System.out.println("===========报文解析：");
            StIso8583 iso8583 = Iso8583Util.iso8583UnPackage(bytes);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private static StIso8583 getSale() {
        StIso8583 stIso8583 = new StIso8583();
        stIso8583.szMsgCode = "0200";
        stIso8583.field2 = "6217007200035723876";
        stIso8583.field3 = "000000";
        stIso8583.field4 = "000000000003";
        stIso8583.field7 = "1109151433";
        stIso8583.field9 = "01000003";
        stIso8583.field11 = "001487";
        stIso8583.field12 = "151433";
        stIso8583.field13 = "1109";
        stIso8583.field14 = "3012";
        stIso8583.field22 = "071";
        stIso8583.field23 = "001";
        stIso8583.field32 = "08752580";
        stIso8583.field33 = "08752580";
        stIso8583.field35 = "6217007200035723876=24112202261020000";
        stIso8583.field41 = "00000058";
        stIso8583.field42 = "303310062110002";
        stIso8583.field48 = "0001";
        stIso8583.field49 = "001";
        stIso8583.field52 = "FBE6207103E6A950";
        stIso8583.field53 = "2600000000000000";
        stIso8583.field55 = "9F2608FBBA4EE085281EE39F2701809F101307010103A00000010A010000000000FD0EA6D89F3704107BD4509F360205B2950500000000009A032312229C01009F02060000000000015F2A02015682027C009F1A0201569F03060000000000009F3303E0E1C89F3501229F1E0831313232333334348408A0000003330101019F090200009F410400000008";
        stIso8583.field60 = "3801                60               ";
        stIso8583.field63 = "";
//        stIso8583.field90 = getField90();
//        stIso8583.field91 = "0";
//        stIso8583.field101 = "0";
//        stIso8583.field104 = "116.244774+40.075423P96150400000404N50007N9281                               260933    4C195D9F20190704";
        stIso8583.field128 = "57703c8d0787fa9a";
        return stIso8583;
    }

    private static StIso8583 getSign() {
        StIso8583 stIso8583 = new StIso8583();
        stIso8583.szMsgCode = "0800";
        stIso8583.field7 = "0921172144";
        stIso8583.field9 = "01000003";
        stIso8583.field11 = "017343";// 11
        stIso8583.field14 = "0030";
        stIso8583.field32 = "08752580";
        stIso8583.field33 = "08752580";
        stIso8583.field41 = "00000058";// 41
        stIso8583.field42 = "303310062110002"; // 42 受卡方标识码(商户代码);
        stIso8583.field48 = "20230921";
//        stIso8583.field60 = "00" + "000111" + "004";// 60
//        stIso8583.field63 = "001";
        stIso8583.field70 = "0001";
        stIso8583.field119 = "0000";
        return stIso8583;
    }
}
