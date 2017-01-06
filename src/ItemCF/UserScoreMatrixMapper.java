package ItemCF;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * ��һ���������û����־���
 * ��������ݸ�ʽΪ:1,101,5.0
 * ��ԭʼ���ݽ���ת��,��ÿ��UserIdΪkey,ItermId:Perference��Ϊvalue���
 */
public class UserScoreMatrixMapper extends Mapper<LongWritable, Text, Text, Text>{ 
	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] str = line.split(",");
		if (str.length == 3) {
			Text k = new Text(str[0]);
			Text v = new Text(str[1]+":"+str[2]);
			context.write(k, v);
		}
	}

}
