package ItemCF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class RecommendMapper extends Mapper<LongWritable, Text, Text, DoubleWritable>{
	
	Text k = new Text();
	DoubleWritable v = new DoubleWritable();
	Map<String, Map<String, Double>> ItemOcurrenceMap = new HashMap<String, Map<String, Double>>();
	
	/*
	 * ��ȡ�ֲ�ʽ�����е�ͬ�־�����г�ʼ������
	 */
	@Override
	protected void setup(Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		if (context.getCacheFiles() != null && context.getCacheFiles().length > 0) {
			//����  101:101 5
			//String path = context.getCacheFiles()[0].toString();
			String path = context.getLocalCacheFiles()[0].getName();
			System.out.println("path="+path);
			File file = new File(path);
		    FileReader fileReade = new FileReader(file);	
		    BufferedReader buff = new BufferedReader(fileReade);
		    
		    String line;
		    while((line = buff.readLine()) != null)
		    {
		    	String[] str = line.split("\t");
		    	if (str.length != 2) {
					continue;
				}
		    	String[] ItemIDs = str[0].split(":");
		    	String itemId1 = ItemIDs[0];
		    	String itemId2 = ItemIDs[1];
		    	
		    	Double perference = Double.parseDouble(str[1]);
		    	Map<String, Double> ItemMap;
		    	if (ItemOcurrenceMap.containsKey(itemId1)) {
					ItemMap = ItemOcurrenceMap.get(itemId1);
				}
		    	else {
					ItemMap = new HashMap<String, Double>();
				}
		    	ItemMap.put(itemId2, perference);
		    	ItemOcurrenceMap.put(itemId1, ItemMap);
		    }
		    buff.close();
		    fileReade.close();
		
		}
	}
	
	/*
	 * ��ȡ��ʼ�����map(ͬ�־���),�����û������ּ�¼�����Ҽ����Ӧ��Ʒ��ϲ�ö�
	 * ��ȡ���־���
	 */
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		//����Ϊ�û����־���    1	103:2.5,101:5,102:3
		String[] str = value.toString().split("\t");
		String userId = str[0];
		
		//ѭ����Ʒͬ�־������,���������Ʒ
		for(Map.Entry<String, Map<String, Double>> rowEntry : ItemOcurrenceMap.entrySet())
		{
			//Ҫ�����û�����ϲ�öȵ�itermId
			String targetItemId = rowEntry.getKey();
			//�������Ʒ�Ѿ������û�������,˵�����û��Ѿ���������Ʒ��,����
			if(value.toString().contains(targetItemId))
			{
				continue;
			}
			//�ܵ÷�
		    double totalScore = 0.0;
		    //�洢targetItemId��ͬ�־���
		    Map<String, Double> curMap = rowEntry.getValue();
		    String[] strAttr = str[1].split(",");
			for(int i = 0; i < strAttr.length; i++)
			{
				String itemId = strAttr[i].split(":")[0];
				double per = Double.parseDouble(strAttr[i].split(":")[1]);
				double ocurrence = 0.0;
				if (curMap.get(itemId) != null) {
					ocurrence = curMap.get(itemId);
				}
				double score = per * ocurrence;
				totalScore += score;
			}
			k.set(userId + ":" + targetItemId);
			v.set(totalScore);
			context.write(k, v);
		}
		
	}

}
