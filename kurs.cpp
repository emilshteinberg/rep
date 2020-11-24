/*
	Автор: Штейнберг Эмиль Эдуардович, группа 0302, 1 курс ФКТИ
	Дата создания программы: 07/11/2020
	Версия: 5.3
	Формулировка:
	Дана матрица M произвольного размера A*B. Программа получает
	на вход A*B значений матрицы и переставляет строки и столбцы
	так, чтобы какой-либо (если таких элементов больше одного)
	максимальный элемент матрицы оказался в левом верхнем ее углу.
	
	Если матрица имеет неправильный размер, то программа задает ей
	размер по максимальному количеству колонок в любой их строк.
	
	Все значений берутся из файла input.txt
*/

#include <iostream>
using namespace std;
#include <fstream>
#include <math.h>

float Length(float* array, int point1, int point2)
{
	return sqrtf(((*(array+point2*2)-*(array+point1*2))*(*(array+point2*2)-*(array+point1*2))) + ((*(array+point2*2+1)-*(array+point1*2+1))*(*(array+point2*2+1)-*(array+point1*2+1))));
}

unsigned char AngleIsRight(float* array, int point1, int point2, int point3, int point4)
{
	if (*(array+point2*2)-*(array+point1*2) == *(array+point3*2)-*(array+point4*2) && *(array+point2*2+1)-*(array+point1*2+1) == *(array+point3*2+1)-*(array+point4*2+1)) return 1;
	else return 0;
}

unsigned char AngleIsRight(float* array, int point0, int point1, int point2)
{
	if (((*(array+point1*2)-*(array+point0*2))*(*(array+point2*2)-*(array+point0*2)) + (*(array+point1*2+1)-*(array+point0*2+1))*(*(array+point2*2+1)-*(array+point0*2+1))) == 0) return 1;
	else return 0;
}

float* InputData(fstream* file, float* array, int* size)
{
	int i = 0, j = 0;
	char t;
	
	(*file) >> noskipws;
	while (1)
	{
		(*file) >> t;
		if (t == '\n')
		{
			cout << t;
			(*size)++;
		}
		if ((*file).eof())
		{
			(*size)++;
			break;
		}
	}
	(*file).close();
	array = new float[(*size)*2];
	
	(*file).open("input.txt", ios::in);
	if ((*file).bad()!=0 || (*file).is_open()==0 || array == NULL)
	{
		cout << "Файл на ввод поврежден или программе не удалось его открыть.\n" <<
		"Работа программы прекращена." << endl;
	}
	else
	{
		(*file) >> skipws;
		for (i = 0; i < (*size); i++)
		{
			for (j = 0; j < 2; j++) *file >> *(array+2*i+j);
		}
	}
	(*file).close();
	return array;
}

int* FindSquares(float* coords, int* sizeCoords, int* indexes, int* sizeIndexes)
{
	int i, j, k, l;
	float len;
	for (i = 0; i < (*sizeCoords)-3; i++)
	for (j = i+1; j < (*sizeCoords)-2; j++)
	{
		len = Length(coords, i, j);
		for (k = j+1; k < (*sizeCoords)-1; k++)
		{
			if (len == Length(coords, j, k) && AngleIsRight(coords, j, i, k))
			{
				for (l = k+1; l < (*sizeCoords); l++)
				{
					if (len == Length(coords, k, l) && AngleIsRight(coords, i, j, k, l))
					{
						(*sizeIndexes)++;
					}
				}
			}
		}
	}
	*indexes = new int[(*sizeIndexes)*4];
	
	int n = 0;
	for (i = 0; i < (*sizeCoords)-3; i++)
	for (j = i+1; j < (*sizeCoords)-2; j++)
	{
		len = Length(coords, i, j);
		for (k = j+1; k < (*sizeCoords)-1; k++)
		{
			if (len == Length(coords, j, k) && AngleIsRight(coords, j, i, k))
			{
				for (l = k+1; l < (*sizeCoords); l++)
				{
					if (len == Length(coords, k, l) && AngleIsRight(coords, i, j, k, l))
					{
						*(indexes+n*4) = i;
						*(indexes+n*4+1) = j;
						*(indexes+n*4+2) = k;
						*(indexes+n*4+3) = l;
						n++;
					}
				}
			}
		}
	}
	return indexes;
}

void OutputData(fstream* file, float* coords, int* indexes)
{
	(*file).open("output.txt", ios::out);
	int i = 0, j = 0;
	if ((*file).bad()!=0 || (*file).is_open()==0 || coords == NULL || indexes == NULL)
	{
		cout << "Файл на вывод поврежден или программе не удалось его создать/открыть.\n" <<
		"Работа программы прекращена." << endl;
	}
	else
	{
		cout << "\nКонец.";
	}
	return;
}

int main()
{
	setlocale(LC_ALL, "Russian");
	float* coords = new float[0];
	int* squares = new int[0];
	int sizeC = 0;
	int sizeS = 0;
	fstream f;
	
	cout << "Автор: Штейнберг Эмиль Эдуардович, группа 0302, 1 курс ФКТИ\n" <<
	"Дата создания программы: 07/11/2020\nВерсия: 5.2\n\nФормулировка:\n" <<
	"Дана матрица M произвольного размера A*B. Программа получает\n" <<
	"на вход A*B значений матрицы и переставляет строки и столбцы\n" <<
	"так, чтобы какой-либо (если таких элементов больше одного)\n" <<
	"максимальный элемент матрицы оказался в левом верхнем ее углу.\n\n" <<
	"Если матрица имеет неправильный размер, то программа задает ей\n" <<
	"размер по максимальному количеству колонок в любой их строк.\n" << endl;
	
	f.open("input.txt", ios::in);
	if (f.bad()!=0 || f.is_open()==0 || a == NULL)
	{
		cout << "Файл на ввод поврежден или программе не удалось его открыть.\n" <<
		"Работа программы прекращена." << endl;
	}
	else
	{
		a = InputData(&f, coords, &sizeC);
		if (size < 4) cout << "Файл содержит недостаточное количество точек (" << size <<" < 4).\nРабота программы прекращается.";
		else
		{
			b = FindSquares(coords, &sizeC, squares, &sizeS);
			OutputData(&f, coords, squares);
			delete [] a;
			delete [] b;
		}
	}
	return 0;
}

