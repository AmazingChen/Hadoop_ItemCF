package ItemCF;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * �ڶ�����������Ʒͬ�־���
 * �����ǵ�һ�������
 * �����keyΪƫ�����������valueΪUserId+�Ʊ��+ItermId1:Perference1,ItermId2:Perference2�� 
 * �����value�У�UserId��Perference�ǲ���Ҫ���ĵģ��۲���Ʒ��ͬ�־���
 * map�׶εĹ������ǽ�ÿ�а�����ItermId������������ȫ���������Ϊkey�����ÿ��key��value��Ϊ1�� 
 */
public class ItemOccurenceMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		//��������ݸ�ʽΪ:1	103:2.5,101:5.0,102:3.0
		String line = value.toString();
		String[] str = line.split("\t");
		if(str.length != 2)
		{
			return;
		}
		
		String[] strTemp = str[1].split(",");
		for(int i = 0; i < strTemp.length; i++)
		{
			String item1 = strTemp[i].split(":")[0];
			for(int j = 0; j < strTemp.length; j++)
			{
				String item2 = strTemp[j].split(":")[0];
				Text k = new Text(item1+":"+item2);
				context.write(k, new IntWritable(1));
			}
		}

	}
}
