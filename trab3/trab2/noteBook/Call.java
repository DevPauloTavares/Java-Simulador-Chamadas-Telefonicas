package trab2.noteBook;

public class Call {
    public String ori,dest;
    public int tempo;
    public String hr;
    public Date d;
    public int ordenar;
    public int flag;



    public Call(String ori,String dest,int tempoMil,String hr,Date d, int flag, int ordenar){
        this.dest = dest;
        this.ori = ori;
        this.tempo= tempoMil;
        this.hr = hr;
        this.d = d;
        this.flag = flag;
        this.ordenar = ordenar;
    }



    public String toString1(){
        Date d = new Date();


        if( flag == 0){
            return ori + " " + d + " " + hr;
        }
        String contr = "" + tempo;
        if (tempo > 60){
            contr = ""+ tempo/60 +" m " + tempo%60;

        }
      return dest + " " + d +" " + hr+ " " + contr  +"s";
    }
}
