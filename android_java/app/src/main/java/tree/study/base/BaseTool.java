package tree.study.base;

import java.util.List;

public class BaseTool {

	public final String[] strListToArray(List<String> list) {
		int length = list.size();
		return list.toArray(new String[length]);
	}

	public final boolean emptyString(String str) {
		return str == null || str.equals("");
	}
}
