package main

import (
	"github.com/willf/bloom"
	"crypto/rand"
	"encoding/base32"
	"time"
	"fmt"
	"os"
	"bufio"
)

func main() {
	start := time.Now()

	filter := bloom.New(1000 * 10000, 2) // load of 20, 2 keys
	count := 1000 * 10000;

	f, err := os.Create("go_random_id.txt")
	check(err)
	defer f.Close()

	w := bufio.NewWriter(f)
	defer w.Flush()

	for count > 0 {
		randomId, _ := generateRandomString(10)
		if !filter.TestString(randomId) {
			w.WriteString(randomId)
			w.WriteString("\n")
			count--;
		}
	}

	elapsed := time.Since(start)
	fmt.Println(elapsed)
}

func generateRandomString(len int) (string, error) {
	b, err := generateRandomBytes(len)
	return base32.StdEncoding.EncodeToString(b)[:len], err
}

func generateRandomBytes(bytes_size int) ([]byte, error) {
	b := make([]byte, bytes_size)
	_, err := rand.Read(b)
	if err != nil {
		return nil, err
	} else {
		return b, nil
	}
}

func check(e error) {
	if e != nil {
		panic(e)
	}
}