import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;


public class DAOGenerator {
	public static final String relRoot="src";//+File.separator;
	public static final String daoPath=relRoot+File.separator+"dao";

	@Test
	public void executeCodeGen() {
		builderCodeGenerator(daoPath,"@@@","accessors","DAO","Schema");
	}
	

	public void builderCodeGenerator(String sourcePath,String contains, String ...exclusions) {
		try {
			Files.walk(Paths.get(sourcePath))
			.filter(Files::isRegularFile)
			.map(path -> path.toString())
			.filter(path->notContainKeywords(path,exclusions))
			.filter(path->path.contains(contains))
			.map(path -> new File(path))
			.forEach(file->{
				 BufferedReader input=null ;
				 PrintWriter out=null;
				 String className=file.getPath().toString().substring(relRoot.length()+1).replace(File.separator,".").replace(".java","");
				 try {
					input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					Class clazz=Class.forName(className);
					List<String> builderCodeGen=builderString(clazz);
					String currentLine = "";
					List<String> sourceCode = new ArrayList<String>();
				     while (currentLine != null) {	
				    	 currentLine = input.readLine();
				    	 if(currentLine!=null&&!currentLine.equals("\n}"))sourceCode.add(currentLine);
				    }
				     input.close();
				     
				     sourceCode.remove(sourceCode.size()-1);
				     sourceCode.addAll(builderCodeGen);
				     sourceCode.add("}");
				     out = new PrintWriter(new BufferedWriter(new FileWriter(file)),false);
				     out.write("");
				     out.flush();
				     out.close();
				     out = new PrintWriter(new BufferedWriter(new FileWriter(file)),true);
				     for(String line:sourceCode) {
				    	 out.println(line);

				     }
				     out.flush();
				     out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}catch (ClassNotFoundException e) {
					e.printStackTrace();
				}finally {
					if(input!=null) {
						try {
							input.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(out!=null) {
						out.close();
					}
				}
				 
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> builderString(Class<?> clazz) {
		List<String> inputCode=new ArrayList<String>();
		List<String> inputMethod=new ArrayList<String>();
		List<String> fields=new ArrayList<String>();
		List<String> build=new ArrayList<String>();
		inputCode.add("\tpublic static class Builder{");	
		inputMethod.add("\t\tpublic Builder"+"(){\n"+"\t\t}");
		inputMethod.add("");		
		build.add("\t\tpublic "+clazz.getSimpleName()+" build(){");
		build.add("\t\t\t"+clazz.getSimpleName()+" "+clazz.getSimpleName().substring(0,1).toLowerCase()+clazz.getSimpleName().substring(1,clazz.getSimpleName().length())+ "=new "+clazz.getSimpleName()+"();");
		for (Field field: clazz.getDeclaredFields()) {
			fields.add("\t\tprivate "+field.getType().getSimpleName()+" "+field.getName()+";");
			inputMethod.add("\t\tpublic Builder with"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1,field.getName().length())+"("+field.getType().getSimpleName()+" "+field.getName()+"){");
			inputMethod.add("\t\t\tthis."+field.getName()+"="+field.getName()+";");
			inputMethod.add("\t\t\treturn this;");
			inputMethod.add("\t\t}");
			inputMethod.add("");
			build.add("\t\t\t"+clazz.getSimpleName().substring(0,1).toLowerCase()+clazz.getSimpleName().substring(1,clazz.getSimpleName().length())+"."+field.getName()+"=this."+field.getName()+";");
		}
		build.add("\t\t\treturn "+clazz.getSimpleName().substring(0,1).toLowerCase()+clazz.getSimpleName().substring(1,clazz.getSimpleName().length())+";");
		build.add("\t\t}");
		inputCode.addAll(fields);
		inputCode.add("");
		inputCode.addAll(inputMethod);
		inputCode.addAll(build);
		inputCode.add("");
		inputCode.add("\t}");

		return inputCode;

	}
	public static boolean notContainKeywords(String input,String ...exclusions) {
		for(String exclusion:exclusions) {
			if(input.contains(exclusion)) return false;
		}
		return true;
	}
}
