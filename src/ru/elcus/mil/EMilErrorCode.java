package ru.elcus.mil;

/**
 * Коды ошибок обмена по линии МКО
 * @author morozo
 *
 */
public enum EMilErrorCode {
	/**
	 *  0 -  обмен завершен без ошибок
	 */
	SX_NOERR,
	/**
	 * 1 - ошибка чётности или ошибка манчестерского кода
	 */
	SX_MEO,
	/**
	 * 2 - неверная пауза перед ответным словом (отсутсвует ответное слово)
	 */
	SX_TOA,
	/**
	 * 3 - нарушена непрерывность сообщения (оствутсвует слово (слова) данных)
	 */
	SX_TOD,
	/**
	 * 4 - число информационных слов больше заданного 
	 */
	SX_ELN,
	/**
	 * 5- неверный адрес ОУ в ОС
	 */
	SX_ERAO,
	/**
	 * 6 - ошибка типа синхроимпульса
	 */
	SX_ESYN,
	/**
	 * 7 - ошибка эхоконтроля при передаче или комбинация нескольких ошибок при приёме 
	 */ 
	SX_EBC; 


	public static EMilErrorCode fromInteger(Integer errCode)
	{
		switch (errCode)
		{
		case 0x00:
			return SX_NOERR;
		case 0x01:
			return SX_MEO;
		case 0x02:
			return SX_TOA;
		case 0x03:
			return SX_TOD;
		case 0x04:
			return SX_ELN;
		case 0x05:
			return SX_ERAO;
		case 0x06:
			return SX_ESYN;
		case 0x07:
			return SX_EBC;

		default:
			return SX_NOERR;

		}
	}

}
