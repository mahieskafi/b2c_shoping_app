package com.srp.ewayspanel.ui.view.textjustify;

import android.graphics.Paint;
import android.view.Gravity;
import android.widget.TextView;

public class TextJustifyUtils {
    private static String HYPHEN_SYMBOL = "-";

    public static void justify(TextView textView) {
        Paint paint = new Paint();

        String[] blocks;
        float spaceOffset = 0;
        float textWrapWidth = 0;

        int spacesToSpread;
        float wrappedEdgeSpace;
        String block;
        String[] lineAsWords;
        String wrappedLine;
        String smb = "";
        Object[] wrappedObj;

        paint.setColor(textView.getCurrentTextColor());
        paint.setTypeface(textView.getTypeface());
        paint.setTextSize(textView.getTextSize());

        textWrapWidth = textView.getWidth();
        spaceOffset = paint.measureText(" ");
        blocks = textView.getText().toString().split("((?<=\n)|(?=\n))");

        if (textWrapWidth < 20) {
            return;
        }

        for (int i = 0; i < blocks.length; i++) {
            block = blocks[i];

            if (block.length() == 0) {
                continue;
            } else if (block.equals("\n")) {
                smb += block;
                continue;
            }

            block = block.trim();

            if (block.length() == 0) continue;

            wrappedObj = TextJustifyUtils.createWrappedLine(block, paint, spaceOffset, textWrapWidth, true, "*"); // --> change
            wrappedLine = ((String) wrappedObj[0]);
            wrappedEdgeSpace = (Float) wrappedObj[1];
            lineAsWords = wrappedLine.split(" ");
            spacesToSpread = (int) (wrappedEdgeSpace != Float.MIN_VALUE ? wrappedEdgeSpace / spaceOffset : 0);

            for (String word : lineAsWords) {
                smb += word + " ";

                if (--spacesToSpread > 0) {
                    smb += " ";
                }
            }

            smb = smb.trim();

            if (blocks[i].length() > 0) {
                blocks[i] = blocks[i].substring(wrappedLine.length());

                if (blocks[i].length() > 0) {
                    smb += "\n";
                }

                i--;
            }
        }

        textView.setGravity(Gravity.LEFT);
        textView.setText(smb);
    }

    protected static Object[] createWrappedLine(String block, Paint paint, float spaceOffset, float maxWidth, boolean hyphenate, String syllableSeparator) {
        float cacheWidth = maxWidth;

        String line = "";

        String[] wordSyllables = new String[0];
        StringBuilder mStringBuilder;
        String cleanWord;
        Integer charCounter = 0;

        wordsLoop:
        for (String dirtyWord : block.split("\\s")) {
            if (hyphenate) {
                mStringBuilder = new StringBuilder();
                wordSyllables = dirtyWord.split(syllableSeparator);

                for (String syllable : wordSyllables) {
                    mStringBuilder.append(syllable);
                }

                cleanWord = mStringBuilder.toString();
            } else {
                cleanWord = dirtyWord;
            }

            cacheWidth = paint.measureText(cleanWord);
            maxWidth -= cacheWidth;

            if (maxWidth <= 0) {
                if (hyphenate) {
                    for (int i = wordSyllables.length - 2; i >= 0; i--) {
                        maxWidth += cacheWidth;
                        mStringBuilder = new StringBuilder();

                        for (int j = 0; j <= i; j++) {
                            mStringBuilder.append(wordSyllables[j]);
                        }

                        cleanWord = mStringBuilder.toString();
                        cacheWidth = paint.measureText(cleanWord + HYPHEN_SYMBOL);
                        maxWidth -= cacheWidth;

                        if (maxWidth > 0) {
                            line += cleanWord + HYPHEN_SYMBOL;
                            charCounter += cleanWord.length() + i + 1;
                            break wordsLoop;
                        }
                    }
                }

                return new Object[]{line, maxWidth + cacheWidth + spaceOffset, charCounter};
            }

            line += cleanWord + " ";
            maxWidth -= spaceOffset;
            charCounter += dirtyWord.length() + 1;
        }

        if (charCounter==0)
            charCounter=1;

        if (block.substring(charCounter - 1).length() == 0) {
            return new Object[]{line.substring(0, line.length() - 1), Float.MIN_VALUE, charCounter - 1};
        }

        return new Object[]{line, maxWidth, charCounter};
    }

    final static String SYSTEM_NEWLINE = "\n";
    final static float COMPLEXITY = 5.12f;
    final static Paint p = new Paint();

    /* @author Mathew Kurian */

    public static void run(final TextView tv, float origWidth) {
        String s = tv.getText().toString();
        p.setTypeface(tv.getTypeface());
        String[] splits = s.split(SYSTEM_NEWLINE);
        float width = origWidth - 5;
        for (int x = 0; x < splits.length; x++)
            if (p.measureText(splits[x]) > width) {
                splits[x] = wrap(splits[x], width, p);
                String[] microSplits = splits[x].split(SYSTEM_NEWLINE);
                for (int y = 0; y < microSplits.length - 1; y++)
                    microSplits[y] = justify(removeLast(microSplits[y], " "), width, p);
                StringBuilder smb_internal = new StringBuilder();
                for (int z = 0; z < microSplits.length; z++)
                    smb_internal.append(microSplits[z] + ((z + 1 < microSplits.length) ? SYSTEM_NEWLINE : ""));
                splits[x] = smb_internal.toString();
            }
        final StringBuilder smb = new StringBuilder();
        for (String cleaned : splits)
            smb.append(cleaned + SYSTEM_NEWLINE);
        tv.setGravity(Gravity.LEFT);
        tv.setText(smb);
    }

    private static String wrap(String s, float width, Paint p) {
        String[] str = s.split("\\s");
        StringBuilder smb = new StringBuilder();
        smb.append(SYSTEM_NEWLINE);
        for (int x = 0; x < str.length; x++) {
            float length = p.measureText(str[x]);
            String[] pieces = smb.toString().split(SYSTEM_NEWLINE);
            try {
                if (p.measureText(pieces[pieces.length - 1]) + length > width)
                    smb.append(SYSTEM_NEWLINE);
            } catch (Exception e) {
            }
            smb.append(str[x] + " ");
        }
        return smb.toString().replaceFirst(SYSTEM_NEWLINE, "");
    }

    private static String removeLast(String s, String g) {
        if (s.contains(g)) {
            int index = s.lastIndexOf(g);
            int indexEnd = index + g.length();
            if (index == 0) return s.substring(1);
            else if (index == s.length() - 1) return s.substring(0, index);
            else
                return s.substring(0, index) + s.substring(indexEnd);
        }
        return s;
    }

    private static String justifyOperation(String s, float width, Paint p) {
        float holder = (float) (COMPLEXITY * Math.random());
        while (s.contains(Float.toString(holder)))
            holder = (float) (COMPLEXITY * Math.random());
        String holder_string = Float.toString(holder);
        float lessThan = width;
        int timeOut = 100;
        int current = 0;
        while (p.measureText(s) < lessThan && current < timeOut) {
            s = s.replaceFirst(" ([^" + holder_string + "])", " " + holder_string + "$1");
            lessThan = p.measureText(holder_string) + lessThan - p.measureText(" ");
            current++;
        }
        String cleaned = s.replaceAll(holder_string, " ");
        return cleaned;
    }

    private static String justify(String s, float width, Paint p) {
        while (p.measureText(s) < width) {
            s = justifyOperation(s, width, p);
        }
        return s;
    }
}

