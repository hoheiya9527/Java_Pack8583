public class StIso8583Wrap {

    public static final int Field_Count_64 = 64;
    public static final int Field_Count_128 = 128;

    /**
     * BCD
     */
    public static final int Type_B = 1;
    /**
     * Numeric
     */
    public static final int Type_N = 2;
    /**
     * ASCII
     */
    public static final int Type_A = 7;

    /**
     * Fix
     */
    public static final int LenLen_fix = 0;
    /**
     * 1 Byte
     */
    public static final int Len_1 = 1;
    /**
     * 2 Byte
     */
    public static final int Len_2 = 2;

    public static final int LenType_B = 1;
    /**
     *
     */
    public static final int LenType_A = 2;

    // 此为定义某域的相关类型,要改变哪个域的类型，或是增加，都在这里改
    public FieldModel getFiled(int fieldIndex) {
        FieldModel FSAtr = new FieldModel();
        switch (fieldIndex) {
            case 1:
                FSAtr.type = Type_N;
                FSAtr.len = 16;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 2:
                FSAtr.type = Type_N;
                FSAtr.len = 19;
                FSAtr.lenLen = Len_1;
                break;
            case 3:
                FSAtr.type = Type_N;
                FSAtr.len = 6;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 4:
                FSAtr.type = Type_N;
                FSAtr.len = 12;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 7:
                FSAtr.type = Type_N;
                FSAtr.len = 10;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 9:
                FSAtr.type = Type_N;
                FSAtr.len = 8;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 11:
                FSAtr.type = Type_N;
                FSAtr.len = 6;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 12:
                FSAtr.type = Type_N;
                FSAtr.len = 6;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 13:
                FSAtr.type = Type_N;
                FSAtr.len = 4;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 14:
                FSAtr.type = Type_N;
                FSAtr.len = 4;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 15:
            case 18:
                FSAtr.type = Type_N;
                FSAtr.len = 4;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 22:
            case 23:
                FSAtr.type = Type_N;
                FSAtr.len = 3;
                FSAtr.lenLen = LenLen_fix;
                break;
//            case 23:
//                FSAtr.type = Type_N;
//                FSAtr.len = 4;
//                FSAtr.lenLen = LenLen_fix;
//                break;
            case 24:
                FSAtr.type = Type_A;
                FSAtr.len = 3;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 25:
                FSAtr.type = Type_N;
                FSAtr.len = 2;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 28:
                FSAtr.type = Type_A;
                FSAtr.len = 9;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 32:
                FSAtr.type = Type_N;
                FSAtr.len = 11;
                FSAtr.lenLen = Len_1;
                break;
            case 33:
                FSAtr.type = Type_N;
                FSAtr.len = 11;
                FSAtr.lenLen = Len_1;
                break;
            case 35:
                FSAtr.type = Type_A;
                FSAtr.len = 37;
                FSAtr.lenLen = Len_1;
                break;
            case 36:
                FSAtr.type = Type_A;
                FSAtr.len = 104;
                FSAtr.lenLen = Len_1;
                break;
            case 37:
                FSAtr.type = Type_A;
                FSAtr.len = 12;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 38:
                FSAtr.type = Type_A;
                FSAtr.len = 6;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 39:
                FSAtr.type = Type_A;
                FSAtr.len = 2;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 41:
                FSAtr.type = Type_A;
                FSAtr.len = 8;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 42:
                FSAtr.type = Type_A;
                FSAtr.len = 15;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 44:
                FSAtr.type = Type_A;
                FSAtr.len = 25;
                FSAtr.lenLen = Len_1;
                break;
            case 45:
                FSAtr.type = Type_A;
                FSAtr.len = 76;

                FSAtr.lenLen = Len_2;
                break;
            case 48:
                FSAtr.type = Type_A;
                FSAtr.len = 999;
                FSAtr.lenLen = Len_2;
                break;
            case 49:
                FSAtr.type = Type_N;
                FSAtr.len = 3;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 50:
                FSAtr.type = Type_N;
                FSAtr.len = 3;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 52:
                FSAtr.type = Type_B;
                FSAtr.len = 8;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 53:
                FSAtr.type = Type_N;
                FSAtr.len = 16;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 54:
                FSAtr.type = Type_A;
                FSAtr.len = 120;
                FSAtr.lenLen = Len_2;
                break;
            case 55:
                FSAtr.type = Type_B;
                FSAtr.len = 256;
                FSAtr.lenLen = Len_2;
                break;
            case 59:
                FSAtr.type = Type_A;
                FSAtr.len = 999;
                FSAtr.lenLen = Len_2;
                break;
            case 60:
                FSAtr.type = Type_A;
                FSAtr.len = 999;
                FSAtr.lenLen = Len_2;
                break;
            case 61:
                FSAtr.type = Type_A;
                FSAtr.len = 999;
                FSAtr.lenLen = Len_2;
                break;
            case 62:
                FSAtr.type = Type_A;
                FSAtr.len = 999;
                FSAtr.lenLen = Len_2;
                break;
            case 63:
                FSAtr.type = Type_A;
                FSAtr.len = 999;
                FSAtr.lenLen = Len_2;
                break;
            case 64:
                FSAtr.type = Type_B;
                FSAtr.len = 8;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 70:
                FSAtr.type = Type_N;
                FSAtr.len = 3;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 90:
                FSAtr.type = Type_N;
                FSAtr.len = 42;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 91:
                FSAtr.type = Type_A;
                FSAtr.len = 1;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 95:
                FSAtr.type = Type_A;
                FSAtr.len = 42;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 100:
                FSAtr.type = Type_A;
                FSAtr.len = 999;
                FSAtr.lenLen = Len_2;
                break; // FIELD100
            case 101:
                FSAtr.type = Type_A;
                FSAtr.len = 1;
                FSAtr.lenLen = Len_1;
                break;
            case 102:
                FSAtr.type = Type_A;
                FSAtr.len = 28;
                FSAtr.lenLen = Len_1;
                break;
            case 104:
                FSAtr.type = Type_A;
                FSAtr.len = 999;
                FSAtr.lenLen = Len_2;
                break;
            case 105:
                FSAtr.type = Type_A;
                FSAtr.len = 50;
                FSAtr.lenLen = LenLen_fix;
                break;
            case 107:
                FSAtr.type = Type_A;
                FSAtr.len = 999;
                FSAtr.lenLen = Len_2;
                break;
            case 119:
                FSAtr.type = Type_N;
                FSAtr.len = 2;
                FSAtr.lenLen = LenLen_fix;
                break;
//            case 121:
//                FSAtr.type = Type_A;
//                FSAtr.len = 999;
//                
//                FSAtr.lenLen = Len_2;
//                break; //
//            case 122:
//                FSAtr.type = Type_A;
//                FSAtr.len = 999;
//                
//                FSAtr.lenLen = Len_2;
//                break; //
//            case 123:
//                FSAtr.type = Type_A;
//                FSAtr.len = 999;
//                
//                FSAtr.lenLen = Len_2;
//                break; //
            case 125:
                FSAtr.type = Type_B;
                FSAtr.len = 64;

                FSAtr.lenLen = Len_2;
                break;
            case 128:
                FSAtr.type = Type_B;
                FSAtr.len = 8;
                FSAtr.lenLen = LenLen_fix;
                break; //
            default:
                return null;
        }
        return FSAtr;
    }
}
