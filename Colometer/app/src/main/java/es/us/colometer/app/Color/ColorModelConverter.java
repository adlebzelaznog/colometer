package es.us.colometer.app.Color;


/**
 Class that performs color model conversions.
 Android camera display previews in NV21 (YUV) format so only
 conversions from YUV format are needed.
 */
public class ColorModelConverter {

    // Attributes ----------------------------------------------------------------------------------
    private int mWidth, mSize;

    // Constructor ---------------------------------------------------------------------------------
    public ColorModelConverter(int height, int width){
        if(height <= 0 || width <= 0 )
            throw new IllegalArgumentException("Invalid height or width. Both must be greater than zero");

        mWidth = width;
        mSize = width*height;
    }

    // Conversion methods --------------------------------------------------------------------------
    /**
     * Convert to the desired format (the one saved into user preferences).
     *
     * @param data      - preview data in YUV420_NV21 format
     * @param format    - desired format.
     * */
    public int[] convert(byte[] data, ColorFormats format){
        int[] res = null;

        if(format.equals(ColorFormats.RGB))
            res = convertYUV420_NV21toARGB8888(data);
        else if(format.equals(ColorFormats.NV21))
            res = parseToInteger(data);

        return res;
    }

    /**
     * Due to no conversion is needed this method only parses byte array to int array (get an integer
     * value from a byte).
     *
     * @param data - - preview data in YUV420_NV21 format
     * */
    private int[] parseToInteger(byte[] data){
        int dataArrayLength = data.length;
        int[] pixels = new int[dataArrayLength];

        for (int i=1; i<dataArrayLength; i++)
            pixels[i] = data[i];

        return pixels;
    }

    /**
     * Converts from YUV420_NV21 to ARGB8888 format.
     * Code from http://en.wikipedia.org/wiki/YUV#Y.27UV420sp_.28NV21.29_to_ARGB8888_conversion
     *
     * @param data - preview data in YUV420_NV21 format
     * */
    private int[] convertYUV420_NV21toARGB8888(byte [] data) {
        int offset = mSize;
        int[] pixels = new int[mSize];
        int u, v, y1, y2, y3, y4;

        // i along Y and the final pixels
        // k along pixels U and V
        for(int i=0, k=0; i < mSize; i+=2, k+=2) {
            y1 = data[i  ]&0xff;
            y2 = data[i+1]&0xff;
            y3 = data[mWidth+i  ]&0xff;
            y4 = data[mWidth+i+1]&0xff;

            v = data[offset+k  ]&0xff;
            u = data[offset+k+1]&0xff;
            v = v-128;
            u = u-128;

            pixels[i  ] = convertYUVtoARGB(y1, u, v);
            pixels[i+1] = convertYUVtoARGB(y2, u, v);
            pixels[mWidth+i  ] = convertYUVtoARGB(y3, u, v);
            pixels[mWidth+i+1] = convertYUVtoARGB(y4, u, v);

            if (i!=0 && (i+2)%mWidth==0)
                i += mWidth;
        }

        return pixels;
    }

    private int convertYUVtoARGB(int y, int u, int v) {
        int r = y + (int)(1.772f*v);
        int g = y - (int)(0.344f*v + 0.714f*u);
        int b = y + (int)(1.402f*u);
        r = r>255? 255 : r<0 ? 0 : r;
        g = g>255? 255 : g<0 ? 0 : g;
        b = b>255? 255 : b<0 ? 0 : b;
        return 0xff000000 | (r<<16) | (g<<8) | b;
    }
}