#include <iostream>
#include <sstream>
#include <cstdio>
#include <vector>
#include <map>
#include <set>
#include <queue>
#include <algorithm>
#include <cstring>
#include <cmath>
#include <string>
#include <cstdlib>
using namespace std;

#define Rep(i,n) for(int i=0;i<(n);++i)
#define For(i,a,b) for(int i=(a);i<=(b);++i)
#define Ford(i,a,b) for(int i=(a);i>=(b);--i)
#define fi first
#define se second
#define pb push_back
#define MP make_pair

typedef pair<int,int> PII;
typedef vector<int> VI;
typedef long long LL;

struct Node {
	LL same;
	LL I, d;
	LL sum;
	int total;
	Node * left, * right;	
};

Node container[900020];
int nct;

int n, q;
int a[100010];
Node *root;

int intersect(int a, int b, int u, int v) {
	a = max(a, u);
	b = min(b, v);
	if(a <= b) return b - a + 1;
	else return 0;	
}

Node *getnode(LL x) {
	Node *p = container + nct++;
	p -> same = -1; 
	p -> I = p -> d = 0;
	p -> sum = x;
	p -> total = 1;
	p -> left = p -> right = NULL;
	return p;
}

void init(Node *&p, int le, int ri) {
	if(le == ri) {
		p = getnode(a[le]);
	}
	else {
		p = container + nct++;
		int mi = (le + ri) / 2;
		init(p->left, le, mi);
		init(p->right, mi + 1, ri);
		p -> same = -1; 
		p -> I = p -> d = 0;
		p -> sum = p -> left -> sum + p -> right -> sum;
		p -> total = p -> left -> total + p -> right -> total;
	}
}

void update(Node *p, LL I, LL d, LL same) {
	if(same != -1) {
		p -> same = same;
		p -> sum = same * p->total;
		p->I = p->d = 0;
	}	
	p->I += I;
	p->d += d;
	p->sum += I * p->total + d * (p->total-1) * (p->total) / 2;
}

void pushdown(Node *p) {
	update(p->left, p->I, p->d, p->same);
	update(p->right, p->I + p->d * p->left->total, p->d, p->same);
	p->I = p->d = 0;
	p->same = -1;
}

LL getsum(Node *p, int onleft, int u, int v) {
	if(onleft + 1 > v) return 0;
	if(onleft + p->total < u) return 0;
	if(u <= onleft + 1 && onleft + p -> total <= v) {
		return p->sum;
	}	
	pushdown(p);
	LL rle = getsum(p->left, onleft, u, v);
	LL rri = getsum(p->right, onleft + p->left->total, u, v);
	return rle + rri;
}

void insert(Node *p, int onleft, int pos, LL val) {
	if(p -> total == 1) {
		LL cur = p->sum;
		if(pos <= onleft + 1) {
			p->left = getnode(val);
			p->right = getnode(cur);
		}
		else {
			p->left = getnode(cur);
			p->right = getnode(val);
		}
		p->I = p->d = 0; p->same = -1;
		p -> sum = cur + val;
		p -> total = 2;
		return;
	}	
	
	pushdown(p);	
	if(pos > p->left->total + onleft) insert(p -> right, onleft + p->left->total, pos, val);
	else insert(p->left, onleft, pos, val);
	p->sum = p->left->sum + p->right->sum;
	p->total = p->left->total + p->right->total;
}

void assign(Node *p, int onleft, int u, int v, LL val) {
	if(onleft + 1 > v) return;
	if(onleft + p->total < u) return;
	if(u <= onleft + 1 && onleft + p -> total <= v) {
		p->I = p->d = 0;
		p->same = val;
		p->sum = p->total * val;
		return;
	}	
	pushdown(p);
	assign(p->left, onleft, u, v, val);
	assign(p->right, onleft + p->left->total, u, v, val);
	p->sum = p->left->sum + p->right->sum;
}

void add(Node *p, int onleft, int u, int v, LL I, LL d) {
	if(onleft + 1 > v) return;
	if(onleft + p->total < u) return;
	if(u <= onleft + 1 && onleft + p -> total <= v) {
		update(p, I, d, -1);
		return;
	}	
	pushdown(p);
	add(p->left, onleft, u, v, I, d);
	add(p->right, onleft + p->left->total, u, v, I + d * intersect(u, v, onleft + 1, onleft + p->left->total), d);
	p->sum = p->left->sum + p->right->sum;
}

int main() {
	scanf("%d%d", &n, &q);
	Rep(i,n) scanf("%d", a+i);
	init(root, 0, n-1);
	Rep(i,q) {
	//	cout << "i = " << i << endl;
		int code;
		scanf("%d", &code);
		if(code == 1) {
			int u, v, x;
			scanf("%d%d%d", &u, &v, &x);	
			assign(root, 0, u, v, x);
		}
		else if(code == 2) {
			int u, v, x;
			scanf("%d%d%d", &u, &v, &x);	
			add(root, 0, u, v, x, x);
		}
		else if(code == 3) {
			int c, x;
			scanf("%d%d", &c, &x);	
			insert( root, 0, c, x);
		}
		else {
			int u, v;
			scanf("%d%d", &u, &v);
			LL res = getsum( root, 0, u, v);
			printf("%lld\n", res);
		}
	}
	return 0;	
}
