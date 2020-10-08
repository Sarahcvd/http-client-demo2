package no.kristiania.httpclient2;

public class QueryString {
    private final String value;
    private String parameterName;

    public QueryString(String queryString) {
        int equalsPos = queryString.indexOf("=");
        value =queryString.substring(equalsPos+1);
        parameterName = queryString.substring(0, equalsPos);
    }

    public String getParameter(String name) {
        if(name.equals(parameterName)) {
            return value;
        }
        return null;
    }
}
