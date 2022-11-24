package com.example.mycalc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycalc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding b; //عمل بايندينج للعناصر بعد تفعيلها بالجريدل

     TextView tvsec, tvmain;
    String pi = "3.14159265";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        b = ActivityMainBinding.inflate(getLayoutInflater());//تعريف الباينديج على الواجهة

        super.onCreate(savedInstanceState);
        setContentView(b.getRoot());

        tvmain = findViewById(R.id.tvmain);
        tvsec = findViewById(R.id.tvsec);


        //OnClickListener
        b.b1.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "1"));

        b.b2.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "2"));

        b.b3.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "3"));

        b.b4.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "4"));

        b.b5.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "5"));

        b.b6.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "6"));

        b.b7.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "7"));

        b.b8.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "8"));

        b.b9.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "9"));

        b.b0.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "0"));


        b.bdot.setOnClickListener(v ->{
            if (! tvmain.getText().toString().contains(".")) {
                tvmain.setText(tvmain.getText() + ".");
            }else {
                Toast.makeText(this, "not Allawed", Toast.LENGTH_SHORT).show();
            }
            });

        b.bac.setOnClickListener(v -> {
            tvmain.setText("");
            tvsec.setText("");
        });

        b.bc.setOnClickListener(v -> {
            String val = tvmain.getText().toString();
            if (val.length()>0) {
                val = val.substring(0, val.length() - 1);
            }else {
                Toast.makeText(this, "thers no thing to delete ", Toast.LENGTH_SHORT).show();
            }

            tvmain.setText(val);
        });

        b.bplus.setOnClickListener(v -> {
            if (! tvmain.getText().toString().contains("+") ) {
                tvmain.setText(tvmain.getText() + "+");

            }else {
                Toast.makeText(this, "not Allawed", Toast.LENGTH_SHORT).show();
            }
        });

        b.bminus.setOnClickListener(v -> {
            if (! tvmain.getText().toString().contains("-") ) {
                tvmain.setText(tvmain.getText() + "-");

            }else {
                Toast.makeText(this, "not Allawed", Toast.LENGTH_SHORT).show();
            }
        });

        b.bdiv.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "÷"));

        b.bmul.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "×"));

        b.bsqrt.setOnClickListener(v -> {
            String val = tvmain.getText().toString();
            double r = Math.sqrt(Double.parseDouble(val));
            tvmain.setText(String.valueOf(r));
        });


        b.bb1.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "("));

        b.bb2.setOnClickListener(v -> tvmain.setText(tvmain.getText() + ")"));

        b.bpi.setOnClickListener(v -> {
            tvsec.setText(b.bpi.getText());
            tvmain.setText(tvmain.getText() + pi);
        });

        b.bsin.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "sin"));

        b.bcos.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "cos"));

        b.btan.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "tan"));

        b.binv.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "^" + "(-1)"));

        b.bfact.setOnClickListener(v -> {
            int val = Integer.parseInt(tvmain.getText().toString());
            int fact = factorial(val);
            tvmain.setText(String.valueOf(fact));
            tvsec.setText(val + "!");
        });

        b.bsquare.setOnClickListener(v -> {
            double d = Double.parseDouble(tvmain.getText().toString());
            double square = d * d;
            tvmain.setText(String.valueOf(square));
            tvsec.setText(d + "²");
        });

        b.bln.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "ln"));

        b.blog.setOnClickListener(v -> tvmain.setText(tvmain.getText() + "log"));

        b.bequal.setOnClickListener(v -> {
            String val = tvmain.getText().toString();
            String replacedstr = val.replace('÷', '/').replace('×', '*');
            double result = eval(replacedstr);
            tvmain.setText((String.valueOf(result)));
            tvsec.setText(val);

        });

    }

    //Factorial function
    int factorial(int n) {
        return (n == 1 || n == 0) ? 1 : n * factorial(n - 1);
    }

    //eval function
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            //Grammer:
            //expression = term | expression '+' term | expression '-' term
            //term = factor | term '*' factor | term '/' factor
            //factor = '+' factor | '-' factor | '(' expression ')'
            //        | number | factorName factor | factor '^' factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); //Addition
                    else if (eat('-')) x -= parseTerm(); //Subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseTerm(); //Multiplication
                    else if (eat('/')) x /= parseTerm(); //Division
                    else return x;
                }
            }

            double parseFactor() {

                if (eat('+')) return parseFactor(); //Unary Plus
                if (eat('-')) return -parseFactor(); //Unary Minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // Parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { //Numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { //Functions
                    while ((ch >= 'a' && ch <= 'z') || ch == '.') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    switch (func) {
                        case "sqrt":
                            x = Math.sqrt(x);
                            break;
                        case "sin":
                            x = Math.sin(Math.toRadians(x));
                            break;
                        case "cos":
                            x = Math.cos(Math.toRadians(x));
                            break;
                        case "tan":
                            x = Math.tan(Math.toRadians(x));
                            break;
                        case "log":
                            x = Math.log10(x);
                            break;
                        case "ln":
                            x = Math.log(x);
                            break;
                        default:
                            throw new RuntimeException("Unknown function: " + func);
                    }

                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                if (eat('^')) x = Math.pow(x, parseFactor()); //Exponentiation
                return x;
            }

        }.parse();

    }
}
