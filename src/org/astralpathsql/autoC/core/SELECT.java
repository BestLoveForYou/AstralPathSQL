package org.astralpathsql.autoC.core;

import org.astralpathsql.autoC.ClassInstanceFactory;
import org.astralpathsql.been.COREINFORMATION;

import static org.astralpathsql.server.MainServer.Mtree;
import static org.astralpathsql.server.MainServer.tree;

public class SELECT {
    public static String select(String sp[]) {
        String sl = sp[1];
        String writeMessage = "-1";
        if (Mtree.containsKey(sp[3])) {
            if (sl.equals("*")) {
                try {
                    if (sp[2].equals("from")) {
                        if (sp[4].equals("where")) {
                            try {
                                if (sp[6].equals("like")) {
                                    String a = Mtree.get(sp[3]).forI(sp[5]);
                                    String b[] = a.split("§");
                                    writeMessage = "";
                                    for (int x=0;x < b.length; x ++) {
                                        COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, b[x]) ;	// 工具类自动设置
                                        String c = emp.getINFO();
                                        String d = c.substring(c.indexOf(sp[5]));
                                        d = d.substring(0,d.indexOf(";"));
                                        d = d.replaceAll(sp[5],"");
                                        if (d.contains(sp[7])) {
                                            writeMessage = writeMessage + c + "§";
                                        }
                                    }
                                    return writeMessage;
                                }
                            } catch (Exception e) {

                            }
                            if (sp[5].equals("id")) {
                                String value = "id:" +sp[7] + "|hiredate:2022-07-27|INFO:null|table:null§";
                                COREINFORMATION c = ClassInstanceFactory.create(COREINFORMATION.class, value) ;	// 工具类自动设置
                                COREINFORMATION resu = (COREINFORMATION) Mtree.get(sp[3]).getId(c);
                                writeMessage = resu.toString();
                                return writeMessage;
                            } else if (sp[6].equals("=")){
                                sp[5] = sp[5] + "'" + sp[7] + "'";
                                String a = Mtree.get(sp[3]).forI(sp[5]);
                                writeMessage = a;
                                return writeMessage;
                            }
                        }

                    } else {
                        return Mtree.get(sp[3]).forW();
                    }
                } catch (Exception e) {
                    String a = Mtree.get(sp[3]).forW();
                    return a;
                }
            }

            if (sl.equals("counts")) {
                if (sp[2].equals("from")) {
                    writeMessage = String.valueOf(Mtree.get(sp[3]).size());
                    return writeMessage;
                }
            } else if(!sl.equals("*")){
                try {
                    if (sl.contains(",")) {
                        try {
                            if (sp[4].equals("where")) {
                                try {
                                    if (sp[6].equals("like")) {
                                        String a = Mtree.get(sp[3]).forI(sp[5]);
                                        String b[] = a.split("§");
                                        writeMessage = "---" + sp[1] + "-------§";
                                        for (int x=0;x < b.length; x ++) {
                                            COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, b[x]) ;	// 工具类自动设置
                                            String c = emp.getINFO();
                                            String d = c.substring(c.indexOf(sp[5]));
                                            d = d.substring(0,d.indexOf(";"));
                                            d = d.replaceAll(sp[5],"");
                                            if (d.contains(sp[7])) {
                                                String r[] = sp[1].split(",");
                                                for (int y = 0 ;y < r.length ; y ++) {
                                                    d = c.substring(c.indexOf(r[y]));
                                                    d = d.substring(0,d.indexOf(";"));
                                                    d = d.replaceAll(r[y] + "'","");
                                                    d = d.replaceAll("'","");
                                                    writeMessage = writeMessage + d + "|";
                                                }
                                                writeMessage = writeMessage + "§";
                                            }
                                        }
                                        return writeMessage;
                                    }
                                }catch (Exception e) {}
                                if (sp[5].equals("id")) {
                                    String value = "id:" +sp[7] + "|hiredate:2022-07-27|INFO:null|table:null§";
                                    COREINFORMATION c = ClassInstanceFactory.create(COREINFORMATION.class, value) ;	// 工具类自动设置
                                    String i[] = sl.split(",");
                                    writeMessage = "";
                                    for (int x =0 ; x < i.length; x ++) {
                                        String a= Mtree.get(sp[3]).getId(c,i[x]);
                                        if (!a.equals("")) {
                                            writeMessage = writeMessage + a;
                                        }
                                    }
                                    writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                    return writeMessage;
                                } else {
                                    String i[] = sl.split(",");
                                    writeMessage = "";
                                    sp[5] = sp[5] + "'" + sp[7] + "'";

                                    for (int x =0 ; x < i.length; x ++) {
                                        String a=  Mtree.get(sp[3]).select(i[x],sp[3],sp[5]);
                                        if (!a.equals("")) {
                                            writeMessage = writeMessage + a;
                                        }
                                    }
                                    writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                    return writeMessage;
                                }

                            }
                        } catch (Exception e) {
                            String i[] = sl.split(",");
                            writeMessage = "";
                            for (int x =0 ; x < i.length; x ++) {
                                String a= Mtree.get(sp[3]).select(i[x],sp[3]);
                                if (!a.equals("")) {
                                    writeMessage = writeMessage + a;
                                }
                            }
                            writeMessage = "+--" + sl + "--+\n" + writeMessage;
                            return writeMessage;
                        }

                    } else {
                        if (sp[4].equals("where")) {
                            try {
                                if (sp[6].equals("like")) {
                                    String a = Mtree.get(sp[3]).forI(sp[5]);
                                    String b[] = a.split("§");
                                    writeMessage = "+--" + sp[1] + "------+§";
                                    for (int x=0;x < b.length; x ++) {
                                        COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, b[x]) ;	// 工具类自动设置
                                        String c = emp.getINFO();
                                        String d = c.substring(c.indexOf(sp[5]));
                                        d = d.substring(0,d.indexOf(";"));
                                        d = d.replaceAll(sp[5],"");
                                        if (d.contains(sp[7])) {
                                            d = c.substring(c.indexOf(sp[1]));
                                            d = d.substring(0,d.indexOf(";"));
                                            d = d.replaceAll(sp[1] + "'","");
                                            d = d.replaceAll("'","");
                                            writeMessage = writeMessage + d + "§";
                                        }
                                    }
                                    return writeMessage;
                                }
                            }catch (Exception e) {}

                            if (sp[5].equals("id")) {
                                String value = "id:" +sp[7] + "|hiredate:2022-07-27|INFO:null|table:null§";
                                COREINFORMATION c = ClassInstanceFactory.create(COREINFORMATION.class, value) ;	// 工具类自动设置
                                writeMessage = Mtree.get(sp[3]).getId(c,sp[1]);
                            } else {
                                sp[5] = sp[5] + "'" + sp[7] + "'";
                                writeMessage = Mtree.get(sp[3]).select(sp[1],sp[3],sp[5]);
                            }
                        } else {
                            writeMessage = Mtree.get(sp[3]).select(sp[1],sp[3]);
                        }
                        writeMessage = "+--" + sl + "--+\n" + writeMessage;
                        return writeMessage;
                    }

                } catch (Exception e) {
                    writeMessage = Mtree.get(sp[3]).select(sp[1],sp[3]);
                    writeMessage = "+--" + sl + "--+\n" + writeMessage;
                    return writeMessage;
                }

            }
        } else {
            if (sl.equals("*")) {
                if (sp[2].equals("from")) {
                    try {
                        if (sp[4].equals("where")) {
                            sp[5] = sp[5] + "'" + sp[7] + "'";
                            String a = tree.forTa(sp[3],sp[5]);
                            writeMessage = a;
                            return  writeMessage;
                        }
                    } catch (Exception e) {

                    }
                }
            } else
            if (sl.equals("counts")) {
                if (sp[2].equals("from")) {
                    writeMessage = String.valueOf(tree.num(sp[3]));
                    return writeMessage;
                }
            } else if(!sp.equals("*")){
                try {
                    if (sl.contains(",")) {
                        try {
                            if (sp[4].equals("where")) {
                                String i[] = sl.split(",");
                                writeMessage = "";
                                sp[5] = sp[5] + "'" + sp[7] + "'";

                                for (int x =0 ; x < i.length; x ++) {
                                    String a=  tree.select(i[x],sp[3],sp[5]);
                                    if (!a.equals("")) {
                                        writeMessage = writeMessage + a;
                                    }
                                }
                                writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                return writeMessage;

                            }
                        } catch (Exception e) {
                            String i[] = sl.split(",");
                            writeMessage = "";
                            for (int x =0 ; x < i.length; x ++) {
                                String a= tree.select(i[x],sp[3]);
                                if (!a.equals("")) {
                                    writeMessage = writeMessage + a;
                                }
                            }
                            writeMessage = "+--" + sl + "--+\n" + writeMessage;
                            return writeMessage;
                        }

                    } else {
                        if (sp[4].equals("where")) {
                            sp[5] = sp[5] + "'" + sp[7] + "'";
                            writeMessage = tree.select(sp[1],sp[3],sp[5]);
                        } else {
                            writeMessage = tree.select(sp[1],sp[3]);
                        }
                        writeMessage = "+--" + sl + "--+\n" + writeMessage;
                        return writeMessage;
                    }

                } catch (Exception e) {
                    writeMessage = tree.select(sp[1],sp[3]);
                    writeMessage = "+--" + sl + "--+\n" + writeMessage;
                    return writeMessage;
                }
            }

            String a = tree.forT(sp[3]);
            writeMessage = a;

        }
        return writeMessage;
    }
}
