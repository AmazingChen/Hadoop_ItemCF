package ItemCF;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * �ڶ�����������Ʒͬ�־���
 * �����ǵ�һ�������
 * ��reduce�׶������ľ��Ǹ���key��value�����ۼ������
 */
public class ItemOccurenceReduce extends Reducer<Text, IntWritable, Text, IntWritable>{
	
	@Override
	protected void reduce(Text key, Iterable<IntWritable> value,
			Context con) throws IOException, InterruptedException {
		
		int sum = 0;
		for(IntWritable v : value)
		{
			sum += v.get();
		}
		con.write(key, new IntWritable(sum));
	}

}
