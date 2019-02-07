package ru.elcus.mil;

/**
 * Порт набора функций из файлов ltmk.c, ltmk.h
 * @author qmor
 *
 */
public class LTMK {

}

class ioctl
{
	/* ioctl command encoding: 32 bits total, command in lower 16 bits,
	 * size of the parameter structure in the lower 14 bits of the
	 * upper 16 bits.
	 * Encoding the size of the parameter structure in the ioctl request
	 * is useful for catching programs compiled with old versions
	 * and to avoid overwriting user space outside the user buffer area.
	 * The highest 2 bits are reserved for indicating the ``access mode''.
	 * NOTE: This limits the max parameter size to 16kB -1 !
	 */
	/*
	 * The following is for compatibility across the various Linux
	 * platforms.  The generic ioctl numbering scheme doesn't really enforce
	 * a type field.  De facto, however, the top 8 bits of the lower 16
	 * bits are indeed used as a type field, so we might just as well make
	 * this explicit here.  Please be sure to use the decoding macros
	 * below from now on.
	 */
	static final int _IOC_NRBITS	=8;
	static final int _IOC_TYPEBITS	=8;
	static final int _IOC_SIZEBITS	=14;
	static final int _IOC_DIRBITS	=2;
	static final int _IOC_NRMASK	=((1 << _IOC_NRBITS)-1);
	static final int _IOC_TYPEMASK	=((1 << _IOC_TYPEBITS)-1);
	static final int _IOC_SIZEMASK	=((1 << _IOC_SIZEBITS)-1);
	static final int _IOC_DIRMASK	=((1 << _IOC_DIRBITS)-1);
	static final int _IOC_NRSHIFT	=0;
	static final int _IOC_TYPESHIFT	=(_IOC_NRSHIFT+_IOC_NRBITS);
	static final int _IOC_SIZESHIFT	=(_IOC_TYPESHIFT+_IOC_TYPEBITS);
	static final int _IOC_DIRSHIFT	=(_IOC_SIZESHIFT+_IOC_SIZEBITS);
	/*
	 * Direction bits.
	 */
	static final int _IOC_NONE	=0;
	static final int _IOC_WRITE	=1;
	static final int _IOC_READ	=2;
	static int _IOC(int dir,int type,int nr,int size) {
		return (((dir)  << _IOC_DIRSHIFT) | ((type) << _IOC_TYPESHIFT) |  ((nr) << _IOC_NRSHIFT) | ((size) << _IOC_SIZESHIFT));
	}
	/* provoke compile error for invalid uses of size argument */
	//extern unsigned int __invalid_size_argument_for_IOC;
	//#define _IOC_TYPECHECK(t) \
	//	((sizeof(t) == sizeof(t[1]) && \
	//	  sizeof(t) < (1 << _IOC_SIZEBITS)) ? \
	//	  sizeof(t) : __invalid_size_argument_for_IOC)
	/* used to create numbers */

	/**
	 * Просто заглушка над макросом
	 * @param size
	 * @return
	 */
	static int _IOC_TYPECHECK(int size)
	{
		return size;
	}
	static int _IO(int type,int nr)		
	{
		return _IOC(_IOC_NONE,(type),(nr),0);
	}
	static int _IOR(int type,int nr,int size)	
	{
		return _IOC(_IOC_READ,(type),(nr),(_IOC_TYPECHECK(size)));
	}
	static int _IOW(int type, int nr, int size)
	{
		return _IOC(_IOC_WRITE,(type),(nr),(_IOC_TYPECHECK(size)));
	}
	static int _IOWR(int type,int nr,int size)
	{
		return _IOC(_IOC_READ|_IOC_WRITE,(type),(nr),(_IOC_TYPECHECK(size)));
	}
	static int _IOR_BAD(int type,int nr,int size)
	{
		return _IOC(_IOC_READ,(type),(nr),(size));
	}
	static int _IOW_BAD(int type,int nr,int size)	
	{
		return _IOC(_IOC_WRITE,(type),(nr),(size));
	}
	static int _IOWR_BAD(int type,int nr,int size)
	{
		return _IOC(_IOC_READ|_IOC_WRITE,(type),(nr),(size));
	}
	/* used to decode ioctl numbers.. */
	static int _IOC_DIR(int nr)		
	{
		return (((nr) >> _IOC_DIRSHIFT) & _IOC_DIRMASK);
	}
	static int _IOC_TYPE(int nr)
	{		
		return (((nr) >> _IOC_TYPESHIFT) & _IOC_TYPEMASK);
	}
	static int _IOC_NR(int nr)	{	return (((nr) >> _IOC_NRSHIFT) & _IOC_NRMASK);}
	static int _IOC_SIZE(int nr) {		return (((nr) >> _IOC_SIZESHIFT) & _IOC_SIZEMASK);}
	/* ...and for the drivers/sound files... */
	static int IOC_IN()		{return (_IOC_WRITE << _IOC_DIRSHIFT); }
	static int IOC_OUT()		{return (_IOC_READ << _IOC_DIRSHIFT);}
	static int IOC_INOUT()	{return ((_IOC_WRITE|_IOC_READ) << _IOC_DIRSHIFT);}
	static int IOCSIZE_MASK()	{return(_IOC_SIZEMASK << _IOC_SIZESHIFT);}
	static int IOCSIZE_SHIFT()	{return (_IOC_SIZESHIFT);}

}