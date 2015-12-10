package com.appforblind;

import java.util.ArrayList;

import org.jtransforms.fft.DoubleFFT_1D;

public class DataObj {

	String[] array;
	String value = "";
	String textView_Value = "";
	String accelerometer_X;
	String accelerometer_Y;
	String accelerometer_Z;

	double signalVectorMagnitude;

	final static double svm_TH = 1.7;

	// ///////////////////////////////FFT

	DoubleFFT_1D doubleFFT;
	ArrayList<Double> dataList;
	int windowSize = 75;
	int blockSize = 128;
	double[] fft;
	double[] magnitude;
	double re, im;
	double freq;
	
	public DataObj(){
		
		dataList = new ArrayList<Double>();	
		
	}

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTextView_Value() {
		return textView_Value;
	}

	public void setTextView_Value(String textView_Value) {
		this.textView_Value = textView_Value;
	}

	public String getAccelerometer_X() {
		return accelerometer_X;
	}

	public void setAccelerometer_X(String accelerometer_X) {
		this.accelerometer_X = accelerometer_X;
	}

	public String getAccelerometer_Y() {
		return accelerometer_Y;
	}

	public void setAccelerometer_Y(String accelerometer_Y) {
		this.accelerometer_Y = accelerometer_Y;
	}

	public String getAccelerometer_Z() {
		return accelerometer_Z;
	}

	public void setAccelerometer_Z(String accelerometer_Z) {
		this.accelerometer_Z = accelerometer_Z;
	}

	public double getSignalVectorMagnitude() {
		return signalVectorMagnitude;
	}

	public void setSignalVectorMagnitude(double signalVectorMagnitude) {
		this.signalVectorMagnitude = signalVectorMagnitude;
	}

	public DoubleFFT_1D getDoubleFFT() {
		return doubleFFT;
	}

	public void setDoubleFFT(DoubleFFT_1D doubleFFT) {
		this.doubleFFT = doubleFFT;
	}

	public ArrayList<Double> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<Double> dataList) {
		this.dataList = dataList;
	}

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public double[] getFft() {
		return fft;
	}

	public void setFft(double[] fft) {
		this.fft = fft;
	}

	public double[] getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double[] magnitude) {
		this.magnitude = magnitude;
	}

	public double getRe() {
		return re;
	}

	public void setRe(double re) {
		this.re = re;
	}

	public double getIm() {
		return im;
	}

	public void setIm(double im) {
		this.im = im;
	}

	public double getFreq() {
		return freq;
	}

	public void setFreq(double freq) {
		this.freq = freq;
	}

	public static double getSvmTh() {
		return svm_TH;
	}
	
}
