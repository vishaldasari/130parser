(define Y (lambda (f)
    ((lambda (h) (lambda (x) ((f (h h)) x)))
     (lambda (h) (lambda (x) ((f (h h)) x))))))

(define fact
  (Y (lambda (f)
       (lambda (n)
         (if (= n 0)
             1
             (* n (f (- n 1))))))))

(fact 12)
