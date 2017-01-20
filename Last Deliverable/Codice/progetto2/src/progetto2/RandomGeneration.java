package progetto2;

import java.util.Random;

public class RandomGeneration {
	
	public int intRandomGeneration(int leftlimit, int rightlimit){
		
		return new Random().nextInt(rightlimit - leftlimit + 1) + leftlimit;
	}
	
	public float floatRandomGeneration(float leftlimit, float rightlimit){
		
		float result = leftlimit + new Random().nextFloat() * (rightlimit - leftlimit);
		
		return result;
		
	}

}
/*Questa classe si occupa di creare dei valori randomici interi o decimali.*/