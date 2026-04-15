package features;

import com.google.i18n.phonenumbers.*;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

public class PhoneLibConfig {
    // 全局单例（必须单例，避免重复加载元数据）
    public static final PhoneNumberUtil PHONE_UTIL = PhoneNumberUtil.getInstance();
    public static final PhoneNumberOfflineGeocoder GEOCODER = PhoneNumberOfflineGeocoder.getInstance();
    public static final PhoneNumberToCarrierMapper CARRIER_MAPPER = PhoneNumberToCarrierMapper.getInstance();
    // 默认国家（中国：CN；美国：US；英国：GB）
    public static final String DEFAULT_REGION = "CN";
    public static final Locale DEFAULT_LOCALE = Locale.CHINA;

    public static void main(String[] args) throws NumberParseException {
        // 示例1：带国家码（+86）
        String phone1 = "+8613800138000";
        PhoneNumber number1 = PhoneLibConfig.PHONE_UTIL.parse(phone1, null);
        // 示例2：不带国家码（默认中国）
        String phone2 = "13800138000";
        PhoneNumber number2 = PhoneLibConfig.PHONE_UTIL.parse(phone2, PhoneLibConfig.DEFAULT_REGION);
        // 示例3：异常处理（格式错误）
        try {
            PhoneNumber number = PhoneLibConfig.PHONE_UTIL.parse("12345", PhoneLibConfig.DEFAULT_REGION);
        } catch (NumberParseException e) {
            System.out.println("解析失败：" + e.getMessage());
        }
        // 快速验证（是否可能是号码）
        boolean possible = PhoneLibConfig.PHONE_UTIL.isPossibleNumber(number2);
        // 完整验证（是否有效）
        boolean valid = PhoneLibConfig.PHONE_UTIL.isValidNumber(number2);
        // 验证指定国家的有效性
        boolean validForRegion = PhoneLibConfig.PHONE_UTIL.isValidNumberForRegion(number2, "CN");
        // 号码格式化   格式化示例
        String e164 = PhoneLibConfig.PHONE_UTIL.format(number2, PhoneNumberUtil.PhoneNumberFormat.E164);
        String national = PhoneLibConfig.PHONE_UTIL.format(number2, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        // 号码类型判断
        PhoneNumberUtil.PhoneNumberType type = PhoneLibConfig.PHONE_UTIL.getNumberType(number2);
        // 类型：MOBILE（手机）、FIXED_LINE（固话）、TOLL_FREE（免费）、VOIP 等
        // 获取中文归属地（省/市） 离线
        String location = PhoneLibConfig.GEOCODER.getDescriptionForNumber(number2, PhoneLibConfig.DEFAULT_LOCALE);
        // 输出：北京市  或者  xx省xx市
        System.out.println(location);
        // 获取中文运营商
        String carrier = PhoneLibConfig.CARRIER_MAPPER.getNameForNumber(number2, PhoneLibConfig.DEFAULT_LOCALE);
        // 输出：中国移动
        System.out.println(carrier);
        // 实时输入格式化（AsYouTypeFormatter）
        AsYouTypeFormatter formatter = PhoneLibConfig.PHONE_UTIL.getAsYouTypeFormatter("CN");
        System.out.println(formatter.inputDigit('1')); // 1
        System.out.println(formatter.inputDigit('3')); // 13
        System.out.println(formatter.inputDigit('8')); // 138
        System.out.println(formatter.inputDigit('0')); // 138 0
        // ... 依次输入，自动格式化
        // 文本中提取号码
        String text = "联系我：13800138000 或 +14155552671";
        Iterable<PhoneNumberMatch> matches = PhoneLibConfig.PHONE_UTIL.findNumbers(text, PhoneLibConfig.DEFAULT_REGION);
        for (PhoneNumberMatch match : matches) {
            System.out.println("提取号码：" + match.rawString() + " → " + match.number());
        }
    }
}