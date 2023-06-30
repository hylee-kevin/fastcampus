package main

import (
        "fmt"
        "os"
        "log"
        "net"
        "net/http"
)
func handler(w http.ResponseWriter, r *http.Request){
    name, err := os.Hostname()
    if err != nil {
         fmt.Printf("error: %v\n", err)
         return
    }
    fmt.Fprintln(w, "hostname: ", name)

    addr, err := net.LookupHost(name)
    if err != nil {
         fmt.Printf("error: %v\n", err)
         return
    }
    fmt.Fprintln(w, "IP: ", addr)

    for _, a := range addr {
        fmt.Println(a)
    }
}
func main() {
    fmt.Fprintln(os.Stdout, "Go!!! Go Application ......")
      http.HandleFunc("/",handler)
      log.Fatal(http.ListenAndServe(":9090",nil))
}
