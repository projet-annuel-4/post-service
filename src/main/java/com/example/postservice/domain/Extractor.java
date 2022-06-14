import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {
    public String getLanguageFromContent(String content){
    
        Pattern p = Pattern.compile("`(.+?)`", Pattern.DOTALL);
        Matcher m = p.matcher(content);
        m.find();

        return m.group(1);
    }


    public String getCodeFromContent(String content){
    
        Pattern p = Pattern.compile("#(.+?)##", Pattern.DOTALL);
        Matcher m = p.matcher(content);
        m.find();

        return m.group(1);
    }
}