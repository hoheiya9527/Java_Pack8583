
public class TestTlv {
    public static void main(String[] args) {
        String str = "9F0605A0000000259F22010EDF05083230313731323331DF060101DF070101DF028190AA94A8C6DAD24F9BA56A27C09B01020819568B81A026BE9FD0A3416CA9A71166ED5084ED91CED47DD457DB7E6CBCD53E560BC5DF48ABC380993B6D549F5196CFA77DFB20A0296188E969A2772E8C4141665F8BB2516BA2C7B5FC91F8DA04E8D512EB0F6411516FB86FC021CE7E969DA94D33937909A53A57F907C40C22009DA7532CB3BE509AE173B39AD6A01BA5BB85DF040103DF0314A7266ABAE64B42A3668851191D49856E17F8FBCD";
        TLVModelList tlvModelList = new TLVModelList(str);
    }
}
