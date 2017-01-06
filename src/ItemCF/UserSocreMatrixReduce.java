package ItemCF;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * ��һ���������û����־���
 * ��map������ۺ�Ϊ�û����־������
 * ��UserId��ͬ���������ּ�¼���л���ƴ�ӣ������key��ȻΪ1��value���磺101:5,102:3,103:2.5
 */
public class UserSocreMatrixReduce extends Reducer<Text, Text, Text, Text>{

	Text v = new Text();
	@Override
	protected void reduce(Text key, Iterable<Text> value, Context context)
			throws IOException, InterruptedException {
		
		String str = new String();
		for(Text v : value)
		{
			str += ",";
			str += v.toString();
		}
		v.set(str.replaceFirst(",", ""));
		context.write(key, v);
	}
}
