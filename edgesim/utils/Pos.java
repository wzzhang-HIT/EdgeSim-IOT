package edgesim.utils;

import java.util.Random;

public class Pos {
    public static void main(String args[])
    {
        Pos pos=new Pos();
        pos.Initialize();
        pos.Search();
    }
    public void Initialize()
    {
        for(int i=0;i<PosNum;i++)
        {
            if(i<PosNum*2/7)
                p_pos[i]=(random.nextDouble()-1)*2;
            else
                p_pos[i]=random.nextDouble()*5;
                p_v[i]=p_best[i]=p_pos[i];
            if(function(g_best)<function(p_best[i]))
            {
                g_best=p_best[i];
            }
        }
    }
    public void Search()
    {
        for(int j=0;j<step;j++)  //迭代
        {
            for(int i=0;i<PosNum;i++)  //更新
            {
                p_v[i]=w*p_v[i]+c1*random.nextDouble()*(p_best[i]-p_pos[i])+c2*random.nextDouble()*(g_best-p_pos[i]);
                if(-2<(p_pos[i]+p_v[i])&&(p_pos[i]+p_v[i])<5)
                    p_pos[i]+=p_v[i];
                if(function(p_best[i])<function(p_pos[i]))
                {
                    p_best[i]=p_pos[i];
                }
                if(function(g_best)<function(p_best[i]))
                {
                    g_best=p_best[i];
                }
                System.out.print(p_pos[i]+" ");
            }
            System.out.println(" ");
        }
        System.out.println(g_best+" "+function(g_best));
    }
    public double function(double x)
    {
        double y=x*x*x-5*x*x-2*x+3;
        return y;
    }
    
    private final int step=100; //迭代次数
    private final int PosNum=70; //粒子数
    private final double w=0.9;//惯性权重
    private final double c1=2;//局部权重参数
    private final double c2=2;//全局权重参数
    
    private double g_best; //全局最优解
    private double p_best[]=new double[PosNum];;//粒子本身历史最优解
    private double[] p_v=new double[PosNum];;//粒子速度
    private double[] p_pos=new double[PosNum];//粒子位置
    
    private Random random=new Random();

}
