package geo.geolab.gravimetry;

public class Matrix {
	/** Liczba wieszy w i kolumn k */
	private Integer w, k;

	/** Tablica zawieraj¹ce pola macierzy */
	private double table[][];

	/** Macierz L i U potrzebne do obliczenia wyznacznika i macierzy odwrotnej */
	private Matrix L, U;

	/**
	 * Buduje tablicê dwuwymiarow¹ o w wierszach i k kolumn
	 * 
	 * @param w
	 *            Liczba wieszy
	 * @param k
	 *            Liczba kolum
	 */
	public Matrix(int w, int k) {
		this.w = w;
		this.k = k;
		table = new double[w][k];
	}

	/**
	 * Zwraca liczbê wierszy w macierzy
	 * 
	 * @return Liczba wierszy
	 */
	public Integer getW() {
		return w;
	}

	/**
	 * Zwraca liczbê kolumn w macierzy
	 * 
	 * @return Liczba kolum
	 */
	public Integer getK() {
		return k;
	}

	/**
	 * Pobiera wartoœæ z wiersza w i kolumny k
	 * 
	 * @param wWiersz
	 * @param kKolumna
	 * @return Wartoœæ z wiersza w i kolumny k
	 */
	public double getValue(int w, int k) {
		return table[w][k];
	}

	/**
	 * Wstawia w wiersz w i kolumnê k wartoœæ value
	 * 
	 * @param w
	 *            Wiersz
	 * @param k
	 *            Kolumna
	 * @param value
	 *            Wstawiana wartoœæ
	 */
	public void setValues(int w, int k, double value) {
		table[w][k] = value;
	}

	/**
	 * Ustawia wszytkie pola w macierzy na wartoœæ 0
	 */
	public void setZero() {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < k; j++) {
				table[i][j] = 0;
			}
		}
	}

	/**
	 * Tworzy macierz jednostkow¹
	 * 
	 * @throws ArithmeticException
	 *             Zg³asz wyj¹tek gdy macierz nie jest kwadratowa
	 */
	public void setIO() throws ArithmeticException {
		if (w != k)
			throw new ArithmeticException("Macierz nie jest kwadratowa");

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < k; j++) {
				if (i == j)
					table[i][j] = 1;
				else
					table[i][j] = 0;
			}
		}
	}

	/**
	 * Dodaje dwie macierze
	 * 
	 * @param matrix
	 *            Macierz która ma byæ dodana
	 * @return Zwraca sumê macierzy
	 * @throws ArithmeticException
	 *             Zwraca wyj¹tek gdy dwie macierze maj¹ ró¿ne wymiary
	 */
	public Matrix add(Matrix matrix) throws ArithmeticException {
		if ((matrix.getW() != this.w) || (matrix.getK() != this.k))
			throw new ArithmeticException("Wymiar macierzy s¹ ró¿ne");

		Matrix returnMatrix = new Matrix(w, k);
		returnMatrix.setZero();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < k; j++) {
				returnMatrix.setValues(i, j,
						table[i][j] + matrix.getValue(i, j));
			}
		}

		return returnMatrix;
	}

	/**
	 * Odejmuje dwie macierze
	 * 
	 * @param matrix
	 *            Macierz która bêdzie odejmowana
	 * @return Zwrara ró¿nicê macierzy
	 * @throws ArithmeticException
	 *             Zwraca wyj¹tek jeœli wymiary macierzy siê nie zgadzaj¹
	 */
	public Matrix substrack(Matrix matrix) throws ArithmeticException {
		if ((matrix.getW() != this.w) || (matrix.getK() != this.k))
			throw new ArithmeticException("Wymiar macierzy s¹ ró¿ne");

		Matrix returnMatrix = new Matrix(w, k);
		returnMatrix.setZero();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < k; j++) {
				returnMatrix.setValues(i, j,
						table[i][j] - matrix.getValue(i, j));
			}
		}

		return returnMatrix;
	}

	/**
	 * Mno¿y macierz przez stala
	 * 
	 * @param skala
	 *            wartoœæ sta³ej
	 * @return macierz pomno¿ona przez sta³¹
	 */
	public Matrix multiply(double skala) {
		Matrix returnMatrix = new Matrix(this.getW(), this.getK());

		for (int i = 0; i < this.getW(); i++) {
			for (int j = 0; j < this.getK(); j++) {
				returnMatrix.setValues(i, j, table[i][j] * skala);
			}
		}

		return returnMatrix;
	}

	/**
	 * Mno¿y podany wiersz macierzy przez stala
	 * 
	 * @param w
	 *            numer wiersza
	 * @param skala
	 *            wartoœæ sta³ej
	 * @return macierz pomno¿ona przez sta³¹
	 */
	public Matrix multiplyLine(Matrix matrix, int w, double skala) {
		for (int i = 0; i < this.getK(); i++) {
			matrix.setValues(w, i, table[w][i] * skala);
		}

		return matrix;
	}

	/**
	 * Mno¿y dwie macierze
	 * 
	 * @param matrix
	 * @return Zwraca iloczyn macierzy
	 * @throws ArithmeticException
	 *             Zwraca wyj¹tek je¿eli liczba kolumn z macierzy A jest ró¿na
	 *             od liczby wierszy w macierzy B
	 */
	public Matrix multiply(Matrix matrix) throws ArithmeticException {
		if (this.getK() != matrix.getW())
			throw new ArithmeticException("Z³e wymiary");

		Matrix returnMatrix = new Matrix(this.getW(), matrix.getK());
		double suma = 0;
		for (int i = 0; i < this.getW(); i++) {
			for (int j = 0; j < matrix.getK(); j++) {
				suma = 0;
				for (int k = 0; k < this.getK(); k++) {
					suma += this.getValue(i, k) * matrix.getValue(k, j);
				}
				returnMatrix.setValues(i, j, suma);
			}
		}

		return returnMatrix;
	}

	/**
	 * Dzieli dwie macierze
	 * 
	 * @param matrix
	 *            Macierz przez któr¹ siê dzieli
	 * @return Iloraz macierzy
	 */
	public Matrix divide(Matrix matrix) {
		return this.multiply(matrix.inverse());
	}

	/**
	 * Rozk³ad macierzy LU potrzebny do obliczenia wyznacznika i odwrotnoœci
	 * macierzy
	 * 
	 */
	private void makeLU() {
		L = new Matrix(w, k);
		U = new Matrix(w, k);

		L.setIO();

		for (int j = 0; j < k; j++) {
			for (int i = 0; i < w; i++) {

				double suma = 0;
				if (i < j) {
					for (int index = 0; index < w; index++) {
						suma += L.getValue(i, index) * U.getValue(index, j);
					}
					U.setValues(i, j, (this.getValue(i, j) - suma));

				}

				if (i == j) {
					for (int index = 0; index < w; index++) {
						suma += L.getValue(i, index) * U.getValue(index, j);
					}
					U.setValues(i, j, this.getValue(i, j) - suma);
				}

				if (i > j) {
					for (int index = 0; index < k; index++) {
						suma += L.getValue(i, index) * U.getValue(index, j);
					}

					L.setValues(i, j,
							(this.getValue(i, j) - suma) / U.getValue(j, j));
				}
			}
		}

	}

	/**
	 * Liczy wyznacznik macierzy
	 * 
	 * @return Wyznacznik macierzy
	 * @throws ArithmeticException
	 *             Zwraca wyj¹tk gdy macierz nie jest kwadratowa
	 */
	public double determinant() throws ArithmeticException {
		if (w != k)
			throw new ArithmeticException("Macierz nie jest kwadratowa");
		else {
			if (L == null || U == null)
				this.makeLU();

			double wyznacznik = 1;
			for (int i = 0; i < w; i++) {
				wyznacznik *= U.getValue(i, i);
			}

			return wyznacznik;
		}
	}

	/**
	 * Odwraca macierz
	 * 
	 * @return Odwrócona macierz
	 * @throws ArithmeticException
	 *             Zwraca wyj¹tek gdy macierz nie jest kwadratowa
	 */
	public Matrix inverse() throws ArithmeticException {

		if (w != k)
			throw new ArithmeticException();
		if (L == null || U == null)
			this.makeLU();

		Matrix I = new Matrix(w, k);
		I.setIO();

		Matrix X = new Matrix(w, k);
		Matrix Y = new Matrix(w, k);

		System.out.println();

		// OBLICZNIE MACIERZY Y
		for (int j = 0; j < k; j++) {
			for (int i = 0; i < w; i++) {
				double suma = 0;
				for (int index = 0; index < w; index++) {
					suma = suma + L.getValue(i, index) * Y.getValue(index, j);
				}
				Y.setValues(i, j, I.getValue(i, j) - suma);

			}
		}

		// OBLICZNIE MACIERZY X
		for (int j = 0; j < k; j++) {
			for (int i = w - 1; i >= 0; i--) {
				double suma = 0;
				for (int index = 0; index < w; index++) {
					suma = suma + U.getValue(i, index) * X.getValue(index, j);

				}

				X.setValues(i, j, (Y.getValue(i, j) - suma) / U.getValue(i, i));

			}
		}

		return X;
	}

	/**
	 * Transponuje macierz
	 * 
	 * @return Transponowana macierz
	 */
	public Matrix transpose() {
		Matrix matrix = new Matrix(k, w);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < k; j++) {
				matrix.setValues(j, i, table[i][j]);
			}
		}
		return matrix;

	}

	/**
	 * Wypisuje macierz w konsoli
	 * 
	 */
	public void display() {
		for (int i = 0; i < w; i++) {
			System.out.println();
			for (int j = 0; j < k; j++) {
				double value = table[i][j];
				System.out.print(value + "        ");
			}
		}
	}

	public double round(double liczba, int dokladnosc) {
		return Math.round(liczba * Math.pow(10, dokladnosc))
				/ Math.pow(10, dokladnosc);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
