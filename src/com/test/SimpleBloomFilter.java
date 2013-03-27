package com.test;

import java.util.BitSet;
//���㷨Ϊ��¡�������ļ�ʵ���㷨
//��ͳ��Bloom filter ��֧�ִӼ�����ɾ����Ա��
//Counting Bloom filter���ڲ����˼��������֧��remove������
//����BitSet��ʵ�֣������Ͽ��ܴ�������
public class SimpleBloomFilter {
    //DEFAULT_SIZEΪ2��25�η�
    private static final int DEFAULT_SIZE = 2 << 24;
    /* ��ͬ��ϣ���������ӣ�һ��Ӧȡ����,seeds���ݹ���7��ֵ����������7�ֲ�ͬ��HASH�㷨 */
    private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };
    //BitSetʵ�����ɡ�������λ�����ɵ�һ��Vector������ϣ����Ч�ʵر�������������ء���Ϣ����Ӧʹ��BitSet.
    //BitSet����С������һ����������Long���ĳ��ȣ�64λ
    private BitSet bits = new BitSet(DEFAULT_SIZE);
    /* ��ϣ�������� */
    private SimpleHash[] func = new SimpleHash[seeds.length];

    public static void main(String[] args) {
       String value = "stone2083@yahoo.cn";
       //����һ��filter�������ʱ�����ù��캯��������ʼ���߸�hash������������Ҫ����Ϣ��
       SimpleBloomFilter filter = new SimpleBloomFilter();
       //�ж��Ƿ���������档��Ϊû�е���add���������Կ϶��Ƿ���false
       System.out.println(filter.contains(value));
       filter.add(value);
       System.out.println(filter.contains(value));
    }
    //���캯��
    public SimpleBloomFilter() {
       for (int i = 0; i < seeds.length; i++) {
           //�������е�hashֵ������seeds.length��hashֵ����7λ��
           //ͨ������SimpleHash.hash(),���Եõ�����7��hash��������ó���hashֵ��
           //����DEFAULT_SIZE(�����ַ����ĳ��ȣ���seeds[i](һ��ָ��������)���ɵõ���Ҫ���Ǹ�hashֵ��λ�á�
           func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
       }
    }

    // ���ַ�����ǵ�bits�У��������ַ�����7��hashֵ����Ϊ1
    public void add(String value) {
       for (SimpleHash f : func) {
           bits.set(f.hash(value), true);
       }
    }

    //�ж��ַ����Ƿ��Ѿ���bits���
    public boolean contains(String value) {
       //ȷ������Ĳ��ǿ�ֵ
       if (value == null) {
           return false;
       }
       boolean ret = true;
       //����7��hash�㷨�¸��Զ�Ӧ��hashֵ�����ж�
       for (SimpleHash f : func) {
           //&&��boolen�������ֻҪ��һ��Ϊ0����Ϊ0������Ҫ���е�λ��Ϊ1���Ŵ�����������档
           //f.hash(value)����hash��Ӧ��λ��ֵ
           //bits.get��������bitset�ж�Ӧposition��ֵ��������hashֵ�Ƿ�Ϊ0��1��
           ret = ret && bits.get(f.hash(value));
       }
       return ret;
    }
    /* ��ϣ������ */
    public static class SimpleHash {
       //capΪDEFAULT_SIZE��ֵ�������ڽ���������ַ������ȡ�
       //seedΪ����hashֵ��һ������key�������Ӧ���涨���seeds����
       private int cap;
       private int seed;

       public SimpleHash(int cap, int seed) {
           this.cap = cap;
           this.seed = seed;
       }

       //����hashֵ�ľ����㷨,hash���������ü򵥵ļ�Ȩ��hash
       public int hash(String value) {
           //int�ķ�Χ�����2��31�η���1���򳬹�ֵ���ø�������ʾ
           int result = 0;
           int len = value.length();
           for (int i = 0; i < len; i++) {
              //���ֺ��ַ�����ӣ��ַ���ת����ΪASCII��
              result = seed * result + value.charAt(i);
              //System.out.println(result+"--"+seed+"*"+result+"+"+value.charAt(i));
           }
       //  System.out.println("result="+result+";"+((cap - 1) & result));
       //  System.out.println(414356308*61+'h');  ִ�д�������Ϊ������Ϊʲô��
           //&��java�е�λ�߼����㣬���ڹ��˸��������������ת���ɷ�����У���
           return (cap - 1) & result;
       }
    }
}